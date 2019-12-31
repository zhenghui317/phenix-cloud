package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.constants.AdminConstants;
import com.phenix.admin.entity.*;
import com.phenix.admin.mapper.SysUserMapper;
import com.phenix.admin.pojo.dto.UserSysAccountDTO;
import com.phenix.admin.service.ISysAccountService;
import com.phenix.admin.service.ISysAuthorityService;
import com.phenix.admin.service.ISysRoleService;
import com.phenix.admin.service.ISysUserService;
import com.phenix.core.exception.PhenixAlertException;
import com.phenix.core.security.authority.PhenixAuthority;
import com.phenix.core.security.authority.PhenixRole;
import com.phenix.core.utils.WebUtils;
import com.phenix.defines.constants.CommonConstants;
import com.phenix.tools.string.StringUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author: zhenghui
 * @date: 2018/10/24 16:33
 * @description:
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysAuthorityService authorityService;
    @Autowired
    private ISysAccountService accountService;

    private final String ACCOUNT_DOMAIN = AdminConstants.ACCOUNT_DOMAIN_ADMIN;

    /**
     * 添加系统用户
     *
     * @param baseSysUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addUser(SysUser baseSysUser, String password) {
        try {
            if (getUserByUsername(baseSysUser.getUserName()) != null) {
                throw new PhenixAlertException("用户名:" + baseSysUser.getUserName() + "已存在!");
            }
            baseSysUser.setCreateTime(LocalDateTime.now());
            baseSysUser.setUpdateTime(baseSysUser.getCreateTime());
            //保存系统用户信息
            sysUserMapper.insert(baseSysUser);
            //默认注册用户名账户
            accountService.register(baseSysUser.getUserId(), baseSysUser.getUserName(), password, AdminConstants.ACCOUNT_TYPE_USERNAME, baseSysUser.getStatus(), ACCOUNT_DOMAIN, null);
            if (StringUtils.matchEmail(baseSysUser.getEmail())) {
                //注册email账号登陆
                accountService.register(baseSysUser.getUserId(), baseSysUser.getEmail(), password, AdminConstants.ACCOUNT_TYPE_EMAIL, baseSysUser.getStatus(), ACCOUNT_DOMAIN, null);
            }
            if (StringUtils.matchMobile(baseSysUser.getMobile())) {
                //注册手机号账号登陆
                accountService.register(baseSysUser.getUserId(), baseSysUser.getMobile(), password, AdminConstants.ACCOUNT_TYPE_MOBILE, baseSysUser.getStatus(), ACCOUNT_DOMAIN, null);
            }
            return true;
        } catch (Exception e) {
            log.error("【SysUser addUser】", e);
            throw e;
        }
    }

    /**
     * 更新系统用户
     *
     * @param baseSysUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUser(SysUser baseSysUser) {
        try {
            if (baseSysUser == null || baseSysUser.getUserId() == null) {
                return false;
            }
            if (baseSysUser.getStatus() != null) {
                accountService.updateStatusByUserId(baseSysUser.getUserId(), ACCOUNT_DOMAIN, baseSysUser.getStatus());
            }
            Boolean success = retBool(sysUserMapper.updateById(baseSysUser));
            return success;
        } catch (Exception e) {
            log.error("【SysUser updateUser】", e);
            throw e;
        }
    }

    /**
     * 添加第三方登录用户
     *
     * @param sysUser
     * @param accountType
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addUserThirdParty(SysUser sysUser, String password, String accountType) {
        try {
            if (accountService.isExist(sysUser.getUserName(), accountType, ACCOUNT_DOMAIN)) {
                log.info("sysUser[{}] is exist", sysUser.getUserName());
                return false;
            }
            sysUser.setUserType(AdminConstants.USER_TYPE_NORMAL);
            sysUser.setCreateTime(LocalDateTime.now());
            sysUser.setUpdateTime(sysUser.getCreateTime());
            //保存系统用户信息
            Boolean success = retBool(sysUserMapper.insert(sysUser));
            if (success) {
                // 注册账号信息
                SysAccount sysAccount = accountService.register(sysUser.getUserId(), sysUser.getUserName(), password, accountType, AdminConstants.ACCOUNT_STATUS_NORMAL, ACCOUNT_DOMAIN, null);
                success = sysAccount != null;
            }
            return success;
        } catch (Exception e) {
            log.error("【SysUser updateUser】", e);
            throw e;
        }
    }

    /**
     * 更新密码
     *
     * @param userId
     * @param password
     */
    @Override
    public Boolean updatePassword(Long userId, String password) {
        return retBool(accountService.updatePasswordByUserId(userId, ACCOUNT_DOMAIN, password));
    }


    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<SysUser> findAllList() {
        List<SysUser> list = sysUserMapper.selectList(new QueryWrapper<>());
        return list;
    }

    /**
     * 依据系统用户Id查询系统用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public SysUser getUserById(Long userId) {
        return sysUserMapper.selectById(userId);
    }

    /**
     * 根据用户ID获取用户信息和权限
     *
     * @param userId
     * @return
     */
    @Override
    public UserSysAccountDTO getUserAccount(Long userId) {
        // 用户权限列表
        List<PhenixAuthority> authorities = Lists.newArrayList();
        // 用户角色列表
        List<PhenixRole> roles = Lists.newArrayList();
        List<SysRole> rolesList = roleService.getUserRoles(userId);
        if (rolesList != null) {
            for (SysRole sysRole : rolesList) {
                PhenixRole phenixRole = new PhenixRole(sysRole.getRoleId().toString(), sysRole.getRoleCode(), sysRole.getRoleName());
                roles.add(phenixRole);
//                // 加入角色标识
//                PhenixAuthority authority = new PhenixAuthority(sysRole.getRoleId().toString(), PhenixAuthorityConstants.AUTHORITY_PREFIX_ROLE + sysRole.getRoleCode(), null, "sysRole");
//                authorities.add(authority);
            }
        }


        //查询系统用户资料
        SysUser baseSysUser = getUserById(userId);

        // 加入用户权限
        List<PhenixAuthority> userGrantedAuthority = authorityService.findAuthorityByUser(userId, CommonConstants.ROOT.equals(baseSysUser.getUserName()));
        if (userGrantedAuthority != null && userGrantedAuthority.size() > 0) {
            authorities.addAll(userGrantedAuthority);
        }
        UserSysAccountDTO userAccount = new UserSysAccountDTO();
        // 昵称
        userAccount.setNickName(baseSysUser.getNickName());
        // 头像
        userAccount.setAvatar(baseSysUser.getAvatar());
        // 权限信息
        userAccount.setAuthorities(authorities);
        // 角色信息
        userAccount.setRoles(roles);
        return userAccount;
    }


    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    @Override
    public SysUser getUserByUsername(String username) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SysUser::getUserName, username);
        SysUser saved = sysUserMapper.selectOne(queryWrapper);
        return saved;
    }


    /**
     * 支持系统用户名、手机号、email登陆
     *
     * @param account
     * @return
     */
    @Override
    public UserSysAccountDTO getUserAccountInfo(String account) {
        if (StringUtils.isBlank(account)) {
            return null;
        }
        Map<String, String> parameterMap = WebUtils.getParameterMap(WebUtils.getHttpServletRequest());
        // 第三方登录标识
        String loginType = parameterMap.get("login_type");
        SysAccount entity = null;
        if (StringUtils.isNotBlank(loginType)) {
            entity = accountService.getAccount(account, loginType, ACCOUNT_DOMAIN);
        } else {
            // 非第三方登录

            //用户名登录
            entity = accountService.getAccount(account, AdminConstants.ACCOUNT_TYPE_USERNAME, ACCOUNT_DOMAIN);

            // 手机号登陆
            if (StringUtils.matchMobile(account)) {
                entity = accountService.getAccount(account, AdminConstants.ACCOUNT_TYPE_MOBILE, ACCOUNT_DOMAIN);
            }
            // 邮箱登陆
            if (StringUtils.matchEmail(account)) {
                entity = accountService.getAccount(account, AdminConstants.ACCOUNT_TYPE_EMAIL, ACCOUNT_DOMAIN);
            }
        }

        // 获取用户详细信息
        if (entity != null) {
            //添加登录日志
            try {
                HttpServletRequest request = WebUtils.getHttpServletRequest();
                if (request != null) {
                    AccountLogs log = new AccountLogs();
                    log.setDomain(ACCOUNT_DOMAIN);
                    log.setUserId(entity.getUserId());
                    log.setAccount(entity.getAccount());
                    log.setAccountId(entity.getAccountId());
                    log.setAccountType(entity.getAccountType());
                    log.setLoginIp(WebUtils.getRemoteAddress(request));
                    log.setLoginAgent(request.getHeader(HttpHeaders.USER_AGENT));
                    accountService.addLoginLog(log);
                }
            } catch (Exception e) {
                log.error("添加登录日志失败:{}", e);
            }
            // 用户权限信息
            UserSysAccountDTO userAccount = getUserAccount(entity.getUserId());
            // 复制账号信息
            BeanUtils.copyProperties(entity, userAccount);
            return userAccount;
        }
        return null;
    }
}
