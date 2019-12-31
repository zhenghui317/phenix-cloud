package com.phenix.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phenix.admin.entity.SysAuthorityRole;
import com.phenix.admin.pojo.dto.AuthoritySysMenuDTO;
import com.phenix.core.security.authority.PhenixAuthority;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统权限-角色关联 服务类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-17
 */
public interface ISysAuthorityRoleService extends IService<SysAuthorityRole> {
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
