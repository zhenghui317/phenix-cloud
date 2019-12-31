package com.phenix.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.*;
import com.phenix.admin.enums.ResourceTypeEnum;
import com.phenix.admin.mapper.*;
import com.phenix.admin.pojo.dto.AuthoritySysApiDTO;
import com.phenix.admin.pojo.dto.AuthoritySysMenuDTO;
import com.phenix.admin.pojo.dto.AuthorityResourceDTO;
import com.phenix.admin.pojo.vo.AuthorityMenuTreeVO;
import com.phenix.admin.pojo.vo.AuthorityMenuVO;
import com.phenix.admin.service.*;
import com.phenix.core.exception.PhenixAlertException;
import com.phenix.core.exception.PhenixException;
import com.phenix.core.security.PhenixOAuthHelper;
import com.phenix.core.security.authority.PhenixAuthority;
import com.phenix.core.security.constants.PhenixAuthorityConstants;
import com.phenix.core.security.token.store.PhenixRedisTokenStore;
import com.phenix.defines.constants.BaseConstants;
import com.phenix.defines.constants.CommonConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.phenix.tools.clone.CloneUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统权限-菜单权限、操作权限、API权限 服务实现类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-17
 */
@Service
public class SysAuthorityServiceImpl extends ServiceImpl<SysAuthorityMapper, SysAuthority> implements ISysAuthorityService {
    @Autowired
    private SysAuthorityMapper sysAuthorityMapper;
    @Autowired
    private ISysAuthorityRoleService authorityRoleService;
    @Autowired
    private ISysAuthorityUserService authorityUserService;
    @Autowired
    private ISysAuthorityAppService authorityAppService;
    @Autowired
    private ISysAuthorityActionService authorityActionService;
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private ISysActionService actionService;
    @Autowired
    private ISysApiService apiService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysAppService appService;
    @Autowired
    private PhenixRedisTokenStore redisTokenStore;

    @Value("${spring.application.name}")
    private String DEFAULT_SERVICE_ID;

    /**
     * 获取访问权限列表
     *
     * @return
     */
    @Override
    public List<AuthorityResourceDTO> findAuthorityResource() {
        List<AuthorityResourceDTO> list = Lists.newArrayList();
        // 已授权资源权限
        List<AuthorityResourceDTO> resourceList = sysAuthorityMapper.selectAllAuthorityResource();
        if (resourceList != null) {
            list.addAll(resourceList);
        }
        return list;
    }

    /**
     * 获取菜单权限列表
     *
     * @return
     */
    @Override
    public List<AuthoritySysMenuDTO> findAuthorityMenu(Integer status) {
        Map map = Maps.newHashMap();
        map.put("status", status);
        List<AuthoritySysMenuDTO> authorities = sysAuthorityMapper.selectAuthorityMenu(map);
        authorities.sort((AuthoritySysMenuDTO h1, AuthoritySysMenuDTO h2) -> h1.getPriority().compareTo(h2.getPriority()));
        return authorities;

    }

    @Override
    public List<AuthoritySysApiDTO> findAuthorityApi(String serviceId) {
        Map map = Maps.newHashMap();
        map.put("serviceId", serviceId);
        map.put("status", BaseConstants.ENABLED);
        List<AuthoritySysApiDTO> authorities = sysAuthorityMapper.selectAuthorityApi(map);
        return authorities;

    }

    /**
     * 查询功能按钮权限列表
     *
     * @param actionId
     * @return
     */
    @Override
    public List<SysAuthorityAction> findAuthorityAction(Long actionId) {
        QueryWrapper<SysAuthorityAction> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SysAuthorityAction::getActionId, actionId);
        return authorityActionService.list(queryWrapper);
    }


    /**
     * 保存或修改权限
     *
     * @param resourceId
     * @param resourceType
     * @return 权限Id
     */
    @Override
    public SysAuthority saveOrUpdateAuthority(Long resourceId, ResourceTypeEnum resourceType) {
        SysAuthority entity = getAuthority(resourceId, resourceType);
        String authority = null;
        if (entity == null) {
            entity = new SysAuthority();
        }
        if (ResourceTypeEnum.menu.equals(resourceType)) {
            SysMenu sysMenu = menuService.getMenu(resourceId);
            authority = PhenixAuthorityConstants.AUTHORITY_PREFIX_MENU + sysMenu.getMenuCode();
            entity.setMenuId(resourceId);
            entity.setStatus(sysMenu.getStatus());
        }
        if (ResourceTypeEnum.action.equals(resourceType)) {
            SysAction operation = actionService.getAction(resourceId);
            authority = PhenixAuthorityConstants.AUTHORITY_PREFIX_ACTION + operation.getActionCode();
            entity.setActionId(resourceId);
            entity.setStatus(operation.getStatus());
        }
        if (ResourceTypeEnum.api.equals(resourceType)) {
            SysApi sysApi = apiService.getApi(resourceId);
            authority = PhenixAuthorityConstants.AUTHORITY_PREFIX_API + sysApi.getApiCode();
            entity.setApiId(resourceId);
            entity.setStatus(sysApi.getStatus());
        }
        if (authority == null) {
            return null;
        }
        // 设置权限标识
        entity.setAuthority(authority);
        if (entity.getAuthorityId() == null) {
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(entity.getCreateTime());
            // 新增权限
            this.save(entity);
        } else {
            // 修改权限
            entity.setUpdateTime(LocalDateTime.now());
            this.updateById(entity);
        }
        return entity;
    }

    /**
     * 移除权限
     *
     * @param resourceId
     * @param resourceType
     * @return
     */
    @Override
    public void removeAuthority(Long resourceId, ResourceTypeEnum resourceType) {
        if (isGranted(resourceId, resourceType)) {
            throw new PhenixAlertException(String.format("资源已被授权,不允许删除!取消授权后,再次尝试!"));
        }
        QueryWrapper<SysAuthority> queryWrapper = buildQueryWrapper(resourceId, resourceType);
        this.remove(queryWrapper);
    }

    /**
     * 获取权限
     *
     * @param resourceId
     * @param resourceType
     * @return
     */
    @Override
    public SysAuthority getAuthority(Long resourceId, ResourceTypeEnum resourceType) {
        if (resourceId == null || resourceType == null) {
            return null;
        }
        QueryWrapper<SysAuthority> queryWrapper = buildQueryWrapper(resourceId, resourceType);
        return this.getOne(queryWrapper);
    }

    /**
     * 是否已被授权
     *
     * @param resourceId
     * @param resourceType
     * @return
     */
    @Override
    public Boolean isGranted(Long resourceId, ResourceTypeEnum resourceType) {
        SysAuthority sysAuthority = getAuthority(resourceId, resourceType);
        if (sysAuthority == null || sysAuthority.getAuthorityId() == null) {
            return false;
        }
        QueryWrapper<SysAuthorityRole> roleQueryWrapper = new QueryWrapper();
        roleQueryWrapper.lambda().eq(SysAuthorityRole::getAuthorityId, sysAuthority.getAuthorityId());
        int roleGrantedCount = authorityRoleService.count(roleQueryWrapper);
        QueryWrapper<SysAuthorityUser> userQueryWrapper = new QueryWrapper();
        userQueryWrapper.lambda().eq(SysAuthorityUser::getAuthorityId, sysAuthority.getAuthorityId());
        int userGrantedCount = authorityUserService.count(userQueryWrapper);
        QueryWrapper<SysAuthorityApp> appQueryWrapper = new QueryWrapper();
        appQueryWrapper.lambda().eq(SysAuthorityApp::getAuthorityId, sysAuthority.getAuthorityId());
        int appGrantedCount = authorityAppService.count(appQueryWrapper);
        return roleGrantedCount > 0 || userGrantedCount > 0 || appGrantedCount > 0;
    }

    /**
     * 构建权限对象
     *
     * @param resourceId
     * @param resourceType
     * @return
     */
    private QueryWrapper<SysAuthority> buildQueryWrapper(Long resourceId, ResourceTypeEnum resourceType) {
        QueryWrapper<SysAuthority> queryWrapper = new QueryWrapper();
        if (ResourceTypeEnum.menu.equals(resourceType)) {
            queryWrapper.lambda().eq(SysAuthority::getMenuId, resourceId);
        }
        if (ResourceTypeEnum.action.equals(resourceType)) {
            queryWrapper.lambda().eq(SysAuthority::getActionId, resourceId);
        }
        if (ResourceTypeEnum.api.equals(resourceType)) {
            queryWrapper.lambda().eq(SysAuthority::getApiId, resourceId);
        }
        return queryWrapper;
    }


    /**
     * 移除应用权限
     *
     * @param appId
     */
    @Override
    public void removeAuthorityApp(Long appId) {
        QueryWrapper<SysAuthorityApp> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysAuthorityApp::getAppId, appId);
        authorityAppService.remove(queryWrapper);
    }

    /**
     * 移除功能按钮权限
     *
     * @param actionId
     */
    @Override
    public void removeAuthorityAction(Long actionId) {
        QueryWrapper<SysAuthorityAction> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SysAuthorityAction::getActionId, actionId);
        authorityActionService.remove(queryWrapper);
    }


    /**
     * 角色授权
     *
     * @param roleId       角色ID
     * @param expireTime   过期时间,null表示长期,不限制
     * @param authorityIds 权限集合
     * @return
     */
    @Override
    public void addAuthorityRole(Long roleId, LocalDateTime expireTime, Collection<Long> authorityIds) {
        if (roleId == null) {
            return;
        }
        // 清空角色已有授权
        QueryWrapper<SysAuthorityRole> roleQueryWrapper = new QueryWrapper();
        roleQueryWrapper.lambda().eq(SysAuthorityRole::getRoleId, roleId);
        authorityRoleService.remove(roleQueryWrapper);
        if (CollectionUtil.isEmpty(authorityIds)) {
            return;
        }
        List<SysAuthorityRole> sysAuthorityRoleList = Lists.newArrayList();
        for (Long id : authorityIds) {
            SysAuthorityRole authority = new SysAuthorityRole();
            sysAuthorityRoleList.add(authority);
            authority.setAuthorityId(id);
            authority.setRoleId(roleId);
            authority.setExpireTime(expireTime);
            authority.setCreateTime(LocalDateTime.now());
            authority.setUpdateTime(authority.getCreateTime());
        }
        // 批量添加授权
        authorityRoleService.saveBatch(sysAuthorityRoleList);
    }

    /**
     * 用户授权
     *
     * @param userId       用户ID
     * @param expireTime   过期时间,null表示长期,不限制
     * @param authorityIds 权限集合
     * @return
     */
    @Override
    public void addAuthorityUser(Long userId, LocalDateTime expireTime, Collection<Long> authorityIds) {
        if (userId == null) {
            return;
        }
        SysUser sysUser = userService.getUserById(userId);
        if (sysUser == null) {
            return;
        }
        if (CommonConstants.ROOT.equals(sysUser.getUserName())) {
            throw new PhenixAlertException("默认用户无需授权!");
        }
        // 获取用户角色列表
        List<Long> roleIds = roleService.getUserRoleIds(userId);
        // 清空用户已有授权
        // 清空角色已有授权
        QueryWrapper<SysAuthorityUser> userQueryWrapper = new QueryWrapper();
        userQueryWrapper.lambda().eq(SysAuthorityUser::getUserId, userId);
        authorityUserService.remove(userQueryWrapper);

        if (CollectionUtil.isEmpty(authorityIds)) {
            return;
        }
        List<SysAuthorityUser> sysAuthorityUserList = Lists.newArrayList();
        for (Long id : authorityIds) {
            if (roleIds != null && roleIds.size() > 0) {
                // 防止重复授权
                if (isGrantedByRoleIds(id, roleIds)) {
                    continue;
                }
            }
            SysAuthorityUser authority = new SysAuthorityUser();
            sysAuthorityUserList.add(authority);
            authority.setAuthorityId(id);
            authority.setUserId(userId);
            authority.setExpireTime(expireTime);
            authority.setCreateTime(LocalDateTime.now());
            authority.setUpdateTime(authority.getCreateTime());
        }
        authorityUserService.saveBatch(sysAuthorityUserList);

    }

    /**
     * 应用授权
     *
     * @param appId        应用ID
     * @param expireTime   过期时间,null表示长期,不限制
     * @param authorityIds 权限集合
     * @return
     */
    @CacheEvict(value = {"apps"}, key = "'client:'+#appId")
    @Override
    public void addAuthorityApp(Long appId, LocalDateTime expireTime, Collection<Long> authorityIds) {
        if (appId == null) {
            return;
        }
        SysApp SysApp = appService.getAppInfo(appId);
        if (SysApp == null) {
            return;
        }
        // 清空应用已有授权
        QueryWrapper<SysAuthorityApp> appQueryWrapper = new QueryWrapper();
        appQueryWrapper.lambda().eq(SysAuthorityApp::getAppId, appId);
        authorityAppService.remove(appQueryWrapper);
        if (CollectionUtil.isEmpty(authorityIds)) {
            return;
        }
        List<SysAuthorityApp> sysAuthorityAppList = Lists.newArrayList();
        for (Long id : authorityIds) {
            SysAuthorityApp authority = new SysAuthorityApp();
            sysAuthorityAppList.add(authority);
            authority.setAuthorityId(id);
            authority.setAppId(appId);
            authority.setExpireTime(expireTime);
            authority.setCreateTime(LocalDateTime.now());
            authority.setUpdateTime(authority.getCreateTime());
        }
        authorityAppService.saveBatch(sysAuthorityAppList);

        // 获取应用最新的权限列表
        List<PhenixAuthority> authorities = findAuthorityByApp(appId);
        // 动态更新tokenStore客户端
        PhenixOAuthHelper.updatePhenixClientAuthorities(redisTokenStore, SysApp.getApiKey(), authorities);
    }

    /**
     * 应用授权-添加单个权限
     *
     * @param appId
     * @param expireTime
     * @param authorityId
     */
    @CacheEvict(value = {"apps"}, key = "'client:'+#appId")
    @Override
    public void addAuthorityApp(Long appId, LocalDateTime expireTime, Long authorityId) {
        SysAuthorityApp authority = new SysAuthorityApp();
        authority.setAppId(appId);
        authority.setAuthorityId(authorityId);
        authority.setExpireTime(expireTime);
        authority.setCreateTime(LocalDateTime.now());
        authority.setUpdateTime(authority.getCreateTime());
        QueryWrapper<SysAuthorityApp> appQueryWrapper = new QueryWrapper();
        appQueryWrapper.lambda()
                .eq(SysAuthorityApp::getAppId, appId)
                .eq(SysAuthorityApp::getAuthorityId, authorityId);
        int count = authorityAppService.count(appQueryWrapper);
        if (count > 0) {
            return;
        }
        authority.setCreateTime(LocalDateTime.now());
        authorityAppService.save(authority);
    }

    /**
     * 添加功能按钮权限
     *
     * @param actionId
     * @param authorityIds
     * @return
     */
    @Override
    public void addAuthorityAction(Long actionId, Collection<Long> authorityIds) {
        if (actionId == null) {
            return;
        }
        // 移除操作已绑定接口
        removeAuthorityAction(actionId);
        if (CollectionUtil.isEmpty(authorityIds)) {
            return;
        }
        List<SysAuthorityAction> sysAuthorityActionList = Lists.newArrayList();
        for (Long authorityId : authorityIds) {
            SysAuthorityAction authority = new SysAuthorityAction();
            sysAuthorityActionList.add(authority);
            authority.setActionId(actionId);
            authority.setAuthorityId(authorityId);
            authority.setCreateTime(LocalDateTime.now());
            authority.setUpdateTime(authority.getCreateTime());
        }
        authorityActionService.saveBatch(sysAuthorityActionList);

    }

    /**
     * 获取应用已授权权限
     *
     * @param appId
     * @return
     */
    @Override
    public List<PhenixAuthority> findAuthorityByApp(Long appId) {
        List<PhenixAuthority> authorities = Lists.newArrayList();
        List<PhenixAuthority> list = authorityAppService.selectAuthorityByApp(appId);
        if (list != null) {
            authorities.addAll(list);
        }
        return authorities;
    }

    /**
     * 获取角色已授权权限
     *
     * @param roleId
     * @return
     */
    @Override
    public List<PhenixAuthority> findAuthorityByRole(Long roleId) {
        return authorityRoleService.selectAuthorityByRole(roleId);
    }

    /**
     * 获取所有可用权限
     *
     * @param type = null 查询全部  type = 1 获取菜单和操作 type = 2 获取API
     * @return
     */
    @Override
    public List<PhenixAuthority> findAuthorityByType(String type) {
        Map map = Maps.newHashMap();
        map.put("type", type);
        map.put("status", 1);
        return sysAuthorityMapper.selectAuthorityAll(map);
    }

    /**
     * 获取用户已授权权限
     *
     * @param userId
     * @param root   超级管理员
     * @return
     */
    @Override
    public List<PhenixAuthority> findAuthorityByUser(Long userId, Boolean root) {
        if (root) {
            // 超级管理员返回所有
            return findAuthorityByType("1");
        }
        List<PhenixAuthority> authorities = Lists.newArrayList();
        List<SysRole> rolesList = roleService.getUserRoles(userId);
        if (rolesList != null) {
            for (SysRole sysRole : rolesList) {
                // 加入角色已授权
                List<PhenixAuthority> roleGrantedAuthority = findAuthorityByRole(sysRole.getRoleId());
                if (roleGrantedAuthority != null && roleGrantedAuthority.size() > 0) {
                    authorities.addAll(roleGrantedAuthority);
                }
            }
        }
        // 加入用户特殊授权
        List<PhenixAuthority> userGrantedAuthority = authorityUserService.selectAuthorityByUser(userId);
        if (userGrantedAuthority != null && userGrantedAuthority.size() > 0) {
            authorities.addAll(userGrantedAuthority);
        }
        // 权限去重
        HashSet h = new HashSet(authorities);
        authorities.clear();
        authorities.addAll(h);
        return authorities;
    }


    /**
     * 获取用户已授权权限详情
     *
     * @param userId
     * @return
     */
    @Override
    public List<AuthoritySysMenuDTO> findAuthorityMenuByUser(Long userId) {
        return this.findAuthorityMenuByUser(userId, false);
    }

    /**
     * 获取用户已授权权限详情
     *
     * @param userId
     * @param root   超级管理员
     * @return
     */
    @Override
    public List<AuthoritySysMenuDTO> findAuthorityMenuByUser(Long userId, Boolean root) {
        if (root) {
            // 超级管理员返回所有
            return findAuthorityMenu(BaseConstants.ENABLED);
        }
        // 用户权限列表
        List<AuthoritySysMenuDTO> authorities = Lists.newArrayList();
        List<SysRole> rolesList = roleService.getUserRoles(userId);
        if (rolesList != null) {
            for (SysRole sysRole : rolesList) {
                // 加入角色已授权
                List<AuthoritySysMenuDTO> roleGrantedAuthority = authorityRoleService.selectAuthorityMenuByRole(sysRole.getRoleId());
                if (roleGrantedAuthority != null && roleGrantedAuthority.size() > 0) {
                    authorities.addAll(roleGrantedAuthority);
                }
            }
        }
        // 加入用户特殊授权
        List<AuthoritySysMenuDTO> userGrantedAuthority = authorityUserService.selectAuthorityMenuByUser(userId);
        if (userGrantedAuthority != null && userGrantedAuthority.size() > 0) {
            authorities.addAll(userGrantedAuthority);
        }
        // 权限去重
        HashSet h = new HashSet(authorities);
        authorities.clear();
        authorities.addAll(h);
        //根据优先级从小到大排序
        authorities.sort((AuthoritySysMenuDTO h1, AuthoritySysMenuDTO h2) -> h1.getPriority().compareTo(h2.getPriority()));
        return authorities;
    }

    @Override
    public AuthorityMenuTreeVO findAuthorityMenuTreeByUser(Long userId) {
        return this.findAuthorityMenuTreeByUser(userId, false);
    }

    @Override
    public AuthorityMenuTreeVO findAuthorityMenuTreeByUser(Long userId, Boolean root) {
        AuthorityMenuTreeVO authorityMenuTreeVO = new AuthorityMenuTreeVO();
        List<AuthoritySysMenuDTO> authorityMenuDTOs = this.findAuthorityMenuByUser(userId, root);
        Long parentId = 0L;
        List<AuthorityMenuVO> rootAuthorityMenuVOList = Lists.newArrayList();
        authorityMenuTreeVO.setChildren(rootAuthorityMenuVOList);
        //查找根菜单
        this.menuDTO2VO(parentId, authorityMenuDTOs, rootAuthorityMenuVOList);
        //查找子菜单
        for (AuthorityMenuVO authorityMenuVO : rootAuthorityMenuVOList) {
            childMenuDTO2VO(authorityMenuVO, authorityMenuDTOs);
        }
        return authorityMenuTreeVO;
    }

    private void menuDTO2VO(Long menuId, Collection<AuthoritySysMenuDTO> authorityMenuDTOs, List<AuthorityMenuVO> authorityMenuVOList) {
        for (AuthoritySysMenuDTO authorityMenuDTO : authorityMenuDTOs) {
            if (!menuId.equals(authorityMenuDTO.getParentId())) {
                continue;
            }
            AuthorityMenuVO authorityMenuVO = CloneUtils.copyColumnProperties(authorityMenuDTO, AuthorityMenuVO.class);
            authorityMenuVOList.add(authorityMenuVO);
        }
    }

    /**
     * 设置子菜单
     * @param authorityMenuVO
     * @param authorityMenuDTOs
     */
    private void childMenuDTO2VO(AuthorityMenuVO authorityMenuVO, Collection<AuthoritySysMenuDTO> authorityMenuDTOs) {
        List<AuthorityMenuVO> childAuthorityMenuVOList = Lists.newArrayList();
        authorityMenuVO.setChildren(childAuthorityMenuVOList);
        //查找子菜单
        Collection<AuthoritySysMenuDTO> childAuthorityMenuDTOs =
                authorityMenuDTOs.stream()
                        .filter(m -> authorityMenuVO.getMenuId().equals(m.getParentId()))
                        .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(childAuthorityMenuDTOs)) {
            return;
        }
        for (AuthoritySysMenuDTO authorityMenuDTO : childAuthorityMenuDTOs) {
            if (!authorityMenuVO.getMenuId().equals(authorityMenuDTO.getParentId())) {
                continue;
            }
            AuthorityMenuVO childAuthorityMenuVO = CloneUtils.copyColumnProperties(authorityMenuDTO, AuthorityMenuVO.class);
            childAuthorityMenuVOList.add(childAuthorityMenuVO);
            //递归调用赋值
            this.childMenuDTO2VO(childAuthorityMenuVO, authorityMenuDTOs);
        }
    }

    /**
     * 检测权限是否被多个角色授权
     *
     * @param authorityId
     * @param roleIds
     * @return
     */
    @Override
    public Boolean isGrantedByRoleIds(Long authorityId, Collection<Long> roleIds) {
        if (CollectionUtil.isEmpty(roleIds)) {
            throw new PhenixException("roleIds is empty");
        }
        QueryWrapper<SysAuthorityRole> roleQueryWrapper = new QueryWrapper();
        roleQueryWrapper.lambda()
                .in(SysAuthorityRole::getRoleId, roleIds)
                .eq(SysAuthorityRole::getAuthorityId, authorityId);
        int count = authorityRoleService.count(roleQueryWrapper);
        return count > 0;
    }

    /**
     * 清理无效数据
     *
     * @param serviceId
     * @param codes
     */
    @Override
    public void clearInvalidApi(Long serviceId, Collection<String> codes) {
        if (serviceId == null) {
            return;
        }
        List<String> invalidApiIds = apiService.listObjs(new QueryWrapper<SysApi>()
                .lambda()
                .select(SysApi::getApiId)
                .eq(SysApi::getServiceId, serviceId)
                .notIn(codes != null && !codes.isEmpty(), SysApi::getApiCode, codes), e -> e.toString());
        if (invalidApiIds != null) {
            // 防止删除默认api
            invalidApiIds.remove("1");
            invalidApiIds.remove("2");
            // 获取无效的权限
            if (invalidApiIds.isEmpty()) {
                return;
            }
            List<String> invalidAuthorityIds = listObjs(new QueryWrapper<SysAuthority>()
                    .lambda()
                    .select(SysAuthority::getAuthorityId)
                    .in(SysAuthority::getApiId, invalidApiIds), e -> e.toString());
            if (invalidAuthorityIds != null && !invalidAuthorityIds.isEmpty()) {
                // 移除关联数据
                authorityAppService.remove(new QueryWrapper<SysAuthorityApp>().lambda().in(SysAuthorityApp::getAuthorityId, invalidAuthorityIds));
                authorityActionService.remove(new QueryWrapper<SysAuthorityAction>().lambda().in(SysAuthorityAction::getAuthorityId, invalidAuthorityIds));
                authorityRoleService.remove(new QueryWrapper<SysAuthorityRole>().lambda().in(SysAuthorityRole::getAuthorityId, invalidAuthorityIds));
                authorityUserService.remove(new QueryWrapper<SysAuthorityUser>().lambda().in(SysAuthorityUser::getAuthorityId, invalidAuthorityIds));
                // 移除权限数据
                this.removeByIds(invalidAuthorityIds);
                // 移除接口资源
                apiService.removeByIds(invalidApiIds);
            }
        }
    }

    @Override
    public List<AuthorityResourceDTO> selectAllAuthorityResource() {
        return sysAuthorityMapper.selectAllAuthorityResource();
    }

    @Override
    public List<PhenixAuthority> selectAuthorityAll(Map map) {
        return sysAuthorityMapper.selectAuthorityAll(map);
    }

    @Override
    public List<AuthoritySysMenuDTO> selectAuthorityMenu(Map map) {
        return sysAuthorityMapper.selectAuthorityMenu(map);
    }

    @Override
    public List<SysAuthorityAction> selectAuthorityAction(Map map) {
        return sysAuthorityMapper.selectAuthorityAction(map);
    }

    @Override
    public List<AuthoritySysApiDTO> selectAuthorityApi(Map map) {
        return sysAuthorityMapper.selectAuthorityApi(map);
    }
}
