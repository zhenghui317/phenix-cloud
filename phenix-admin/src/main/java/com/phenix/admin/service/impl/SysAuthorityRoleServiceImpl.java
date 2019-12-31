package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.SysAuthorityRole;
import com.phenix.admin.mapper.SysAuthorityRoleMapper;
import com.phenix.admin.pojo.dto.AuthoritySysMenuDTO;
import com.phenix.admin.service.ISysAuthorityRoleService;
import com.phenix.core.security.authority.PhenixAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统权限-角色关联 服务实现类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-17
 */
@Service
public class SysAuthorityRoleServiceImpl extends ServiceImpl<SysAuthorityRoleMapper, SysAuthorityRole> implements ISysAuthorityRoleService {

    @Autowired
    private SysAuthorityRoleMapper sysAuthorityRoleMapper;

    @Override
    public List<PhenixAuthority> selectAuthorityByRole(Long roleId) {
        return sysAuthorityRoleMapper.selectAuthorityByRole(roleId);
    }

    @Override
    public List<AuthoritySysMenuDTO> selectAuthorityMenuByRole(Long roleId) {
        return sysAuthorityRoleMapper.selectAuthorityMenuByRole(roleId);
    }
}
