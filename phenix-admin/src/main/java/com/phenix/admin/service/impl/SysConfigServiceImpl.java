package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phenix.admin.entity.SysConfig;
import com.phenix.admin.entity.SysDictData;
import com.phenix.admin.mapper.SysConfigMapper;
import com.phenix.admin.service.ISysConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 参数配置表 服务实现类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-25
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

    @Override
    public SysConfig getSysConfigByConfigKey(String configKey) {
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysConfig :: getConfigKey, configKey);
        return this.getOne(queryWrapper);
    }
}
