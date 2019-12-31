package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.SysMenu;
import com.phenix.admin.enums.ResourceTypeEnum;
import com.phenix.admin.mapper.SysMenuMapper;
import com.phenix.admin.service.ISysActionService;
import com.phenix.admin.service.ISysAuthorityService;
import com.phenix.admin.service.ISysMenuService;
import com.phenix.core.exception.PhenixAlertException;
import com.phenix.defines.constants.BaseConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhenghui
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private ISysAuthorityService authorityService;

    @Autowired
    private ISysActionService actionService;

    @Value("${spring.application.name}")
    private String DEFAULT_SERVICE_ID;

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<SysMenu> findAllList() {
        List<SysMenu> list = sysMenuMapper.selectList(new QueryWrapper<>());
        //根据优先级从小到大排序
        list.sort((SysMenu h1, SysMenu h2) -> h1.getPriority().compareTo(h2.getPriority()));
        return list;
    }

    /**
     * 根据主键获取菜单
     *
     * @param menuId
     * @return
     */
    @Override
    public SysMenu getMenu(Long menuId) {
        return sysMenuMapper.selectById(menuId);
    }

    /**
     * 检查菜单编码是否存在
     *
     * @param menuCode
     * @return
     */
    @Override
    public Boolean isExist(String menuCode) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SysMenu::getMenuCode, menuCode);
        int count = sysMenuMapper.selectCount(queryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 添加菜单资源
     *
     * @param sysMenu
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addMenu(SysMenu sysMenu) {
        try {
            if (isExist(sysMenu.getMenuCode())) {
                throw new PhenixAlertException(String.format("%s编码已存在!", sysMenu.getMenuCode()));
            }
            if (sysMenu.getParentId() == null) {
                sysMenu.setParentId(0L);
            }
            if (sysMenu.getPriority() == null) {
                sysMenu.setPriority(0);
            }
            if (sysMenu.getStatus() == null) {
                sysMenu.setStatus(BaseConstants.ENABLED);
            }
            if (sysMenu.getIsPersist() == null) {
                sysMenu.setIsPersist(BaseConstants.FALSE);
            }
            sysMenu.setServiceId(DEFAULT_SERVICE_ID);
            sysMenu.setCreateTime(LocalDateTime.now());
            sysMenu.setUpdateTime(sysMenu.getCreateTime());
            Boolean success = retBool(sysMenuMapper.insert(sysMenu));
            if (success) {
                // 同步权限表里的信息
                authorityService.saveOrUpdateAuthority(sysMenu.getMenuId(), ResourceTypeEnum.menu);
            }
            return success;
        } catch (Exception e) {
            log.error("【SysMenu addMenu】", e);
            throw e;
        }
    }

    /**
     * 修改菜单资源
     *
     * @param sysMenu
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateMenu(SysMenu sysMenu) {
        try {
            SysMenu saved = getMenu(sysMenu.getMenuId());
            if (saved == null) {
                throw new PhenixAlertException(String.format("%s信息不存在!", sysMenu.getMenuId()));
            }
            if (!saved.getMenuCode().equals(sysMenu.getMenuCode())) {
                // 和原来不一致重新检查唯一性
                if (isExist(sysMenu.getMenuCode())) {
                    throw new PhenixAlertException(String.format("%s编码已存在!", sysMenu.getMenuCode()));
                }
            }
            if (sysMenu.getParentId() == null) {
                sysMenu.setParentId(0L);
            }
            if (sysMenu.getPriority() == null) {
                sysMenu.setPriority(0);
            }
            sysMenu.setUpdateTime(LocalDateTime.now());
            Boolean success = retBool(sysMenuMapper.updateById(sysMenu));

            if (success) {
                // 同步权限表里的信息
                authorityService.saveOrUpdateAuthority(sysMenu.getMenuId(), ResourceTypeEnum.menu);
            }
            return success;
        } catch (Exception e) {
            log.error("【SysMenu updateMenu】", e);
            throw e;
        }
    }


    /**
     * 移除菜单
     *
     * @param menuId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeMenu(Long menuId) {
        try {

            SysMenu sysMenu = getMenu(menuId);
            if (sysMenu != null && BaseConstants.TRUE.equals(sysMenu.getIsPersist())) {
                throw new PhenixAlertException(String.format("保留数据,不允许删除!"));
            }
            // 移除菜单权限
            authorityService.removeAuthority(menuId, ResourceTypeEnum.menu);
            // 移除功能按钮和相关权限
            actionService.removeByMenuId(menuId);
            // 移除菜单信息
            sysMenuMapper.deleteById(menuId);
            return true;
        }catch (Exception e){
            log.error("【SysMenu removeMenu】", e);
            throw e;
        }
    }


}
