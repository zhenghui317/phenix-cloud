package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.SysRole;
import com.phenix.admin.entity.SysRoleUser;
import com.phenix.admin.mapper.SysRoleUserMapper;
import com.phenix.admin.service.ISysRoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统角色-用户关联 服务实现类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-17
 */
@Service
public class SysRoleUserServiceImpl extends ServiceImpl<SysRoleUserMapper, SysRoleUser> implements ISysRoleUserService {

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Override
    public List<SysRole> selectRoleUserList(Long userId) {
        return sysRoleUserMapper.selectRoleUserList(userId);
    }

    @Override
    public List<Long> selectRoleUserIdList(Long userId) {
        return sysRoleUserMapper.selectRoleUserIdList(userId);
    }
}
