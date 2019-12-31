package com.phenix.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phenix.admin.entity.SysMenu;

import java.util.List;

/**
 * 菜单资源管理
 * @author zhenghui
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 查询列表
     * @return
     */
    List<SysMenu> findAllList();

    /**
     * 根据主键获取菜单
     *
     * @param menuId
     * @return
     */
    SysMenu getMenu(Long menuId);

    /**
     * 检查菜单编码是否存在
     *
     * @param menuCode
     * @return
     */
    Boolean isExist(String menuCode);


    /**
     * 添加菜单资源
     *
     * @param sysMenu
     * @return
     */
    Boolean addMenu(SysMenu sysMenu);

    /**
     * 修改菜单资源
     *
     * @param sysMenu
     * @return
     */
    Boolean updateMenu(SysMenu sysMenu);

    /**
     * 移除菜单
     *
     * @param menuId
     * @return
     */
    Boolean removeMenu(Long menuId);
}
