package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.SysAccount;
import com.phenix.admin.entity.AccountLogs;
import com.phenix.admin.mapper.AccountLogsMapper;
import com.phenix.admin.mapper.SysAccountMapper;
import com.phenix.admin.service.ISysAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 通用账号
 *
 * @author zhenghui
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysAccountServiceImpl extends ServiceImpl<SysAccountMapper, SysAccount> implements ISysAccountService {

    @Autowired
    private SysAccountMapper sysAccountMapper;
    @Autowired
    private AccountLogsMapper accountLogsMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * 根据主键获取账号信息
     *
     * @param accountId
     * @return
     */
    @Override
    public SysAccount getAccountById(Long accountId) {
        return sysAccountMapper.selectById(accountId);
    }

    /**
     * 获取账号信息
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    @Override
    public SysAccount getAccount(String account, String accountType, String domain) {
        QueryWrapper<SysAccount> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SysAccount::getAccount, account)
                .eq(SysAccount::getAccountType, accountType)
                .eq(SysAccount::getDomain, domain);
        return sysAccountMapper.selectOne(queryWrapper);
    }

    /**
     * 注册账号
     *
     * @param userId
     * @param account
     * @param password
     * @param accountType
     * @param status
     * @param domain
     * @param registerIp
     * @return
     */
    @Override
    public SysAccount register(Long userId, String account, String password, String accountType, Integer status, String domain, String registerIp) {
        if (isExist(account, accountType, domain)) {
            // 账号已被注册
            throw new RuntimeException(String.format("account=[%s],domain=[%s]", account, domain));
        }
        //加密
        String encodePassword = passwordEncoder.encode(password);
        SysAccount entity = SysAccount.builder()
                .userId(userId)
                .account(account)
                .password(encodePassword)
                .accountType(accountType)
                .domain(domain)
                .registerIp(registerIp)
                .status(status)
                .build();
        sysAccountMapper.insert(entity);
        return entity;
    }


    /**
     * 检测账号是否存在
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    @Override
    public Boolean isExist(String account, String accountType, String domain) {
        QueryWrapper<SysAccount> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SysAccount::getAccount, account)
                .eq(SysAccount::getAccountType, accountType)
                .eq(SysAccount::getDomain, domain);
        int count = sysAccountMapper.selectCount(queryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 删除账号
     *
     * @param accountId
     * @return
     */
    @Override
    public int removeAccount(Long accountId) {
        return sysAccountMapper.deleteById(accountId);
    }


    /**
     * 更新账号状态
     *
     * @param accountId
     * @param status
     */
    @Override
    public int updateStatus(Long accountId, Integer status) {
        SysAccount entity = SysAccount.builder()
                .accountId(accountId)
                .status(status)
                .build();
        return sysAccountMapper.updateById(entity);
    }

    /**
     * 根据用户更新账户状态
     *
     * @param userId
     * @param domain
     * @param status
     */
    @Override
    public int updateStatusByUserId(Long userId, String domain, Integer status) {
        if (status == null) {
            return 0;
        }
        SysAccount entity = SysAccount.builder().status(status).build();
        QueryWrapper<SysAccount> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(SysAccount::getDomain, domain)
                .eq(SysAccount::getUserId, userId);
        return sysAccountMapper.update(entity, wrapper);
    }

    /**
     * 重置用户密码
     *
     * @param userId
     * @param domain
     * @param password
     */
    @Override
    public int updatePasswordByUserId(Long userId, String domain, String password) {
        String encoderPassword = passwordEncoder.encode(password);
        SysAccount entity =  SysAccount.builder().password(encoderPassword).build();
        QueryWrapper<SysAccount> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(SysAccount::getUserId, userId)
                .eq(SysAccount::getDomain, domain);
        return sysAccountMapper.update(entity, wrapper);
    }

    /**
     * 根据用户ID删除账号
     *
     * @param userId
     * @param domain
     * @return
     */
    @Override
    public int removeAccountByUserId(Long userId, String domain) {
        QueryWrapper<SysAccount> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(SysAccount::getUserId, userId)
                .eq(SysAccount::getDomain, domain);
        return sysAccountMapper.delete(wrapper);
    }


    /**
     * 添加登录日志
     *
     * @param log
     */
    @Override
    public void addLoginLog(AccountLogs log) {
        log.setLoginTime(LocalDateTime.now());
        log.setCreateUserId(0L);
        log.setUpdateUserId(0L);
        QueryWrapper<AccountLogs> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(AccountLogs::getAccountId, log.getAccountId())
                .eq(AccountLogs::getUserId, log.getUserId());
        int count = accountLogsMapper.selectCount(queryWrapper);
        log.setLoginNums(count + 1);
        accountLogsMapper.insert(log);
    }
}
