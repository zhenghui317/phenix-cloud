package com.phenix.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phenix.admin.entity.SysAction;

import java.util.List;

/**
 * 操作资源管理
 *
 * @author zhenghui
 */
public interface ISysActionService extends IService<SysAction> {

    /**
     * 根据主键获取操作
     *
     * @param actionId
     * @return
     */
    SysAction getAction(Long actionId);

    /**
     * 查询菜单下所有操作
     *
     * @param menuId
     * @return
     */
    List<SysAction> findListByMenuId(Long menuId);

    /**
     * 检查操作编码是否存在
     *
     * @param actionCode
     * @return
     */
    Boolean isExist(String actionCode);


    /**
     * 添加操作资源
     *
     * @param sysAction
     * @return
     */
    Boolean addAction(SysAction sysAction);

    /**
     * 修改操作资源
     *
     * @param sysAction
     * @return
     */
    Boolean updateAction(SysAction sysAction);

    /**
     * 移除操作
     *
     * @param actionId
     * @return
     */
    Boolean removeAction(Long actionId);

    /**
     * 移除菜单相关资源
     *
     * @param menuId
     */
    Boolean removeByMenuId(Long menuId);
}
