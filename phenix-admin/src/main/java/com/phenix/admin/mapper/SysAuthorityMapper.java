package com.phenix.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phenix.admin.entity.SysAuthority;
import com.phenix.admin.entity.SysAuthorityAction;
import com.phenix.admin.pojo.dto.AuthoritySysApiDTO;
import com.phenix.admin.pojo.dto.AuthoritySysMenuDTO;
import com.phenix.admin.pojo.dto.AuthorityResourceDTO;
import com.phenix.core.security.authority.PhenixAuthority;

import java.util.List;
import java.util.Map;

/**
 * @author zhenghui
 */
public interface SysAuthorityMapper extends BaseMapper<SysAuthority> {

    /**
     * 查询所有资源授权列表
     * @return
     */
    List<AuthorityResourceDTO> selectAllAuthorityResource();

    /**
     * 查询已授权权限列表
     *
     * @param map
     * @return
     */
    List<PhenixAuthority> selectAuthorityAll(Map map);


    /**
     * 获取菜单权限
     *
     * @param map
     * @return
     */
    List<AuthoritySysMenuDTO> selectAuthorityMenu(Map map);

    /**
     * 获取操作权限
     *
     * @param map
     * @return
     */
    List<SysAuthorityAction> selectAuthorityAction(Map map);

    /**
     * 获取API权限
     *
     * @param map
     * @return
     */
    List<AuthoritySysApiDTO> selectAuthorityApi(Map map);


}
