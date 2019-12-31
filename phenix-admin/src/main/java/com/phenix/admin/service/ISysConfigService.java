package com.phenix.admin.service;

import com.phenix.admin.entity.SysConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phenix.admin.entity.SysDictData;

import java.util.List;

/**
 * <p>
 * 参数配置表 服务类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-25
 */
public interface ISysConfigService extends IService<SysConfig> {

    /**
     * 根据字典编码查询
     *
     * @param configKey 配置名
     * @return
     */
    SysConfig getSysConfigByConfigKey(String configKey);
}
