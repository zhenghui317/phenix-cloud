package com.phenix.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phenix.admin.entity.SysAuthority;
import com.phenix.admin.entity.SysAuthorityAction;
import com.phenix.admin.enums.ResourceTypeEnum;
import com.phenix.admin.pojo.dto.AuthoritySysApiDTO;
import com.phenix.admin.pojo.dto.AuthoritySysMenuDTO;
import com.phenix.admin.pojo.dto.AuthorityResourceDTO;
import com.phenix.admin.pojo.vo.AuthorityMenuTreeVO;
import com.phenix.core.security.authority.PhenixAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统权限-菜单权限、操作权限、API权限 服务类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-17
 */
public interface ISysAuthorityService extends IService<SysAuthority> {
    /**
     * 获取访问权限列表
     *
     * @return
     */
    List<AuthorityResourceDTO> findAuthorityResource();

    /**
     * 获取菜单权限列表
     *
     * @param status
     * @return
     */
    List<AuthoritySysMenuDTO> findAuthorityMenu(Integer status);


    /**
     * 获取API权限列表
     *
     * @param serviceId
     * @return
     */
    List<AuthoritySysApiDTO> findAuthorityApi(String serviceId);


    /**
     * 查询功能按钮权限列表
     *
     * @param actionId
     * @return
     */
    List<SysAuthorityAction> findAuthorityAction(Long actionId);


    /**
     * 保存或修改权限
     *
     * @param resourceId
     * @param resourceTypeEnum
     * @return 权限Id
     */
    SysAuthority saveOrUpdateAuthority(Long resourceId, ResourceTypeEnum resourceTypeEnum);


    /**
     * 获取权限
     *
     * @param resourceId
     * @param resourceTypeEnum
     * @return
     */
    SysAuthority getAuthority(Long resourceId, ResourceTypeEnum resourceTypeEnum);

    /**
     * 移除权限
     *
     * @param resourceId
     * @param resourceTypeEnum
     * @return
     */
    void removeAuthority(Long resourceId, ResourceTypeEnum resourceTypeEnum);

    /**
     * 移除应用权限
     *
     * @param appId
     */
    void removeAuthorityApp(Long appId);


    /**
     * 移除功能按钮权限
     * @param actionId
     */
    void  removeAuthorityAction(Long actionId);

    /**
     * 是否已被授权
     *
     * @param resourceId
     * @param resourceTypeEnum
     * @return
     */
    Boolean isGranted(Long resourceId, ResourceTypeEnum resourceTypeEnum);

    /**
     * 角色授权
     *
     * @param
     * @param roleId       角色ID
     * @param expireTime   过期时间,null表示长期,不限制
     * @param authorityIds 权限集合
     * @return 权限标识
     */
    void addAuthorityRole(Long roleId, LocalDateTime expireTime, Collection<Long> authorityIds);


    /**
     * 用户授权
     *
     * @param
     * @param userId       用户ID
     * @param expireTime   过期时间,null表示长期,不限制
     * @param authorityIds 权限集合
     * @return 权限标识
     */
    void addAuthorityUser(Long userId, LocalDateTime expireTime, Collection<Long> authorityIds);


    /**
     * 应用授权
     *
     * @param
     * @param appId        应用ID
     * @param expireTime   过期时间,null表示长期,不限制
     * @param authorityIds 权限集合
     * @return
     */
    void addAuthorityApp(Long appId, LocalDateTime expireTime, Collection<Long> authorityIds);

    /**
     * 应用授权-添加单个权限
     *
     * @param appId
     * @param expireTime
     * @param authorityId
     */
    void addAuthorityApp(Long appId, LocalDateTime expireTime, Long authorityId);

    /**
     * 添加功能按钮权限
     *
     * @param actionId
     * @param authorityIds
     * @return
     */
    void addAuthorityAction(Long actionId, Collection<Long> authorityIds);

    /**
     * 获取应用已授权权限
     *
     * @param appId
     * @return
     */
    List<PhenixAuthority> findAuthorityByApp(Long appId);

    /**
     * 获取角色已授权权限
     *
     * @param roleId
     * @return
     */
    List<PhenixAuthority> findAuthorityByRole(Long roleId);

    /**
     * 获取用户已授权权限
     *
     * @param userId
     * @param root
     * @return
     */
    List<PhenixAuthority> findAuthorityByUser(Long userId, Boolean root);

    /**
     * 获取开放对象权限
     *
     * @param type = null 查询全部  type = 1 获取菜单和操作 type = 2 获取API
     * @return
     */
    List<PhenixAuthority> findAuthorityByType(String type);

    /**
     * 获取用户已授权权限详情
     *
     * @param userId
     * @return
     */
    List<AuthoritySysMenuDTO> findAuthorityMenuByUser(Long userId);

    /**
     * 获取用户已授权权限详情
     *
     * @param userId
     * @param root
     * @return
     */
    List<AuthoritySysMenuDTO> findAuthorityMenuByUser(Long userId, Boolean root);

    /**
     * 获取用户已授权权限树形列表详情
     *
     * @param userId
     * @return
     */
    AuthorityMenuTreeVO findAuthorityMenuTreeByUser(Long userId);

    /**
     * 获取用户已授权权限树形列表详情
     *
     * @param userId
     * @param root
     * @return
     */
    AuthorityMenuTreeVO findAuthorityMenuTreeByUser(Long userId, Boolean root);

    /**
     * 检测全是是否被多个角色授权
     *
     * @param authorityId
     * @param roleIds
     * @return
     */
    Boolean isGrantedByRoleIds(Long authorityId, Collection<Long> roleIds);


    /**
     * 清理无效权限
     * @param serviceId
     * @param codes
     */
    void clearInvalidApi(Long serviceId, Collection<String> codes);


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
