package com.phenix.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phenix.admin.entity.SysApp;
import com.phenix.core.security.PhenixClientDetails;

/**
 * 应用信息管理
 *
 * @author zhenghui
 */
public interface ISysAppService extends IService<SysApp> {
    

    /**
     * 获取app信息
     *
     * @param appId
     * @return
     */
    SysApp getAppInfo(Long appId);

    /**
     * 获取app和应用信息
     *
     * @param clientId
     * @return
     */
    PhenixClientDetails getAppClientInfo(String clientId);


    /**
     * 更新应用开发新型
     *
     * @param client
     */
    void updateAppClientInfo(PhenixClientDetails client);

    /**
     * 添加应用
     *
     * @param sysApp 应用
     * @return 应用信息
     */
    SysApp addAppInfo(SysApp sysApp);

    /**
     * 修改应用
     *
     * @param sysApp 应用
     * @return 应用信息
     */
    SysApp updateInfo(SysApp sysApp);


    /**
     * 重置秘钥
     *
     * @param appId
     * @return
     */
    String restSecret(Long appId);

    /**
     * 删除应用
     *
     * @param appId
     * @return
     */
    void removeApp(Long appId);

}
