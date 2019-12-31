package com.phenix.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phenix.admin.entity.SysApi;

import java.util.List;

/**
 * 接口资源管理
 *
 * @author zhenghui
 */
public interface ISysApiService extends IService<SysApi> {

    /**
     * 查询列表
     *
     * @return
     */
    List<SysApi> findAllList(String serviceId);

    /**
     * 根据主键获取接口
     *
     * @param apiId
     * @return
     */
    SysApi getApi(Long apiId);


    /**
     * 检查接口编码是否存在
     *
     * @param apiCode
     * @return
     */
    Boolean isExist(String apiCode);

    /**
     * 添加接口
     *
     * @param sysApi
     * @return
     */
    Boolean addApi(SysApi sysApi);

    /**
     * 修改接口
     *
     * @param sysApi
     * @return
     */
    Boolean updateApi(SysApi sysApi);

    /**
     * 查询接口
     *
     * @param apiCode
     * @return
     */
    SysApi getApi(String apiCode);

    /**
     * 移除接口
     *
     * @param apiId
     * @return
     */
    Boolean removeApi(Long apiId);


    /**
     * 获取数量
     *
     * @param queryWrapper
     * @return
     */
    int getCount(QueryWrapper<SysApi> queryWrapper);



}
