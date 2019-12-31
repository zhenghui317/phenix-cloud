package com.phenix.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.SysRole;
import com.phenix.admin.entity.SysRoleUser;
import com.phenix.admin.entity.SysUser;
import com.phenix.admin.mapper.SysRoleMapper;
import com.phenix.admin.service.ISysRoleService;
import com.phenix.admin.service.ISysRoleUserService;
import com.phenix.admin.service.ISysUserService;
import com.phenix.core.exception.PhenixAlertException;
import com.phenix.defines.constants.BaseConstants;
import com.phenix.defines.constants.CommonConstants;
import com.phenix.tools.string.StringUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


/**
 * @author zhenghui
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private ISysRoleUserService roleUserService;
    @Autowired
    private ISysUserService userService;


    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<SysRole> findAllList() {
        List<SysRole> list = sysRoleMapper.selectList(new QueryWrapper<>());
        return list;
    }

    /**
     * 获取角色信息
     *
     * @param roleId
     * @return
     */
    @Override
    public SysRole getRole(Long roleId) {
        return sysRoleMapper.selectById(roleId);
    }


    /**
     * 添加角色
     *
     * @param sysRole 角色
     * @return
     */
    @Override
    public SysRole addRole(SysRole sysRole) {
        if (isExist(sysRole.getRoleCode())) {
            throw new PhenixAlertException(String.format("%s编码已存在!", sysRole.getRoleCode()));
        }
        if (sysRole.getStatus() == null) {
            sysRole.setStatus(BaseConstants.ENABLED);
        }
        if (sysRole.getIsPersist() == null) {
            sysRole.setIsPersist(BaseConstants.FALSE);
        }
        sysRole.setCreateTime(LocalDateTime.now());
        sysRole.setUpdateTime(sysRole.getCreateTime());
        sysRoleMapper.insert(sysRole);
        return sysRole;
    }

    /**
     * 更新角色
     *
     * @param sysRole 角色
     * @return
     */
    @Override
    public SysRole updateRole(SysRole sysRole) {
        SysRole saved = this.getById(sysRole.getRoleId());
        if (sysRole == null) {
            throw new PhenixAlertException("信息不存在!");
        }
        if (!saved.getRoleCode().equals(sysRole.getRoleCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(sysRole.getRoleCode())) {
                throw new PhenixAlertException(String.format("%s编码已存在!", sysRole.getRoleCode()));
            }
        }
        sysRole.setUpdateTime(LocalDateTime.now());
        sysRoleMapper.updateById(sysRole);
        return sysRole;
    }

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @return
     */
    @Override
    public void removeRole(Long roleId) {
        if (roleId == null) {
            return;
        }
        SysRole sysRole = this.getById(roleId);
        if (sysRole != null && sysRole.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new PhenixAlertException(String.format("保留数据,不允许删除"));
        }
        int count = getCountByRole(roleId);
        if (count > 0) {
            throw new PhenixAlertException("该角色下存在授权人员,不允许删除!");
        }
        sysRoleMapper.deleteById(roleId);
    }

    /**
     * 检测角色编码是否存在
     *
     * @param roleCode
     * @return
     */
    @Override
    public Boolean isExist(String roleCode) {
        if (StringUtils.isBlank(roleCode)) {
            throw new PhenixAlertException("roleCode不能为空!");
        }
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysRole::getRoleCode, roleCode);
        return sysRoleMapper.selectCount(queryWrapper) > 0;
    }

    /**
     * 用户添加角色
     *
     * @param userId
     * @param roles
     * @return
     */
    @Override
    public void saveUserRoles(Long userId, Collection<Long> roles) {
        if (userId == null) {
            return;
        }
        if (CollectionUtil.isEmpty(roles)) {
            return;
        }
        SysUser sysUser = userService.getUserById(userId);
        if (sysUser == null) {
            return;
        }
        if (CommonConstants.ROOT.equals(sysUser.getUserName())) {
            throw new PhenixAlertException("默认用户无需分配!");
        }
        // 先清空,在添加
        removeUserRoles(userId);
        List<SysRoleUser> sysRoleUserList = Lists.newArrayList();
        for (Long roleId : roles) {
            SysRoleUser sysRoleUser = new SysRoleUser();
            sysRoleUserList.add(sysRoleUser);
            sysRoleUser.setUserId(userId);
            sysRoleUser.setRoleId(roleId);
        }
        // 批量保存
        roleUserService.saveBatch(sysRoleUserList);
    }

    /**
     * 角色添加成员
     *
     * @param roleId
     * @param userIds
     */
    @Override
    public void saveRoleUsers(Long roleId, Collection<Long> userIds) {
        if (roleId == null) {
            return;
        }
        if (CollectionUtil.isEmpty(userIds)) {
            return;
        }
        // 先清空,在添加
        removeRoleUsers(roleId);
        List<SysRoleUser> sysRoleUserList = Lists.newArrayList();
        for (Long userId : userIds) {
            SysRoleUser sysRoleUser = new SysRoleUser();
            sysRoleUserList.add(sysRoleUser);
            sysRoleUser.setUserId(userId);
            sysRoleUser.setRoleId(roleId);
        }
        // 批量保存
        roleUserService.saveBatch(sysRoleUserList);
    }

    /**
     * 查询角色成员
     *
     * @return
     */
    @Override
    public List<SysRoleUser> findRoleUsers(Long roleId) {
        QueryWrapper<SysRoleUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysRoleUser::getRoleId, roleId);
        return roleUserService.list(queryWrapper);
    }

    /**
     * 获取角色所有授权组员数量
     *
     * @param roleId
     * @return
     */
    @Override
    public int getCountByRole(Long roleId) {
        QueryWrapper<SysRoleUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysRoleUser::getRoleId, roleId);
        int result = roleUserService.count(queryWrapper);
        return result;
    }

    /**
     * 获取组员角色数量
     *
     * @param userId
     * @return
     */
    @Override
    public int getCountByUser(Long userId) {
        QueryWrapper<SysRoleUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysRoleUser::getUserId, userId);
        int result = roleUserService.count(queryWrapper);
        return result;
    }

    /**
     * 移除角色所有组员
     *
     * @param roleId
     * @return
     */
    @Override
    public void removeRoleUsers(Long roleId) {
        QueryWrapper<SysRoleUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysRoleUser::getRoleId, roleId);
        roleUserService.remove(queryWrapper);
    }

    /**
     * 移除组员的所有角色
     *
     * @param userId
     * @return
     */
    @Override
    public void removeUserRoles(Long userId) {
        QueryWrapper<SysRoleUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysRoleUser::getUserId, userId);
        roleUserService.remove(queryWrapper);
    }

    /**
     * 检测是否存在
     *
     * @param userId
     * @param roleId
     * @return
     */
    @Override
    public Boolean isExist(Long userId, Long roleId) {
        QueryWrapper<SysRoleUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysRoleUser::getRoleId, roleId);
        queryWrapper.lambda().eq(SysRoleUser::getUserId, userId);
        roleUserService.remove(queryWrapper);
        int result = roleUserService.count(queryWrapper);
        return result > 0;
    }


    /**
     * 获取组员角色
     *
     * @param userId
     * @return
     */
    @Override
    public List<SysRole> getUserRoles(Long userId) {
        List<SysRole> sysRoles = roleUserService.selectRoleUserList(userId);
        return sysRoles;
    }

    /**
     * 获取用户角色ID列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<Long> getUserRoleIds(Long userId) {
        return roleUserService.selectRoleUserIdList(userId);
    }


}
