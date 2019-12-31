package com.phenix.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phenix.admin.entity.SysAuthorityRole;
import com.phenix.admin.pojo.dto.AuthoritySysMenuDTO;
import com.phenix.core.security.authority.PhenixAuthority;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhenghui
 */
public interface SysAuthorityRoleMapper extends BaseMapper<SysAuthorityRole> {

    /**
     * 获取角色已授权权限
     *
     * @param roleId
     * @return
     */
    List<PhenixAuthority> selectAuthorityByRole(@Param("roleId") Long roleId);

    /**
     * 获取角色菜单权限
     *
     * @param roleId
     * @return
     */
    List<AuthoritySysMenuDTO> selectAuthorityMenuByRole(@Param("roleId") Long roleId);
}
