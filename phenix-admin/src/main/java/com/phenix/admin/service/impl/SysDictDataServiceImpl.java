package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phenix.admin.entity.SysDictData;
import com.phenix.admin.entity.SysDictType;
import com.phenix.admin.mapper.SysDictDataMapper;
import com.phenix.admin.service.ISysDictDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-25
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {

    /**
     * 根据字典编码查询
     * @param dictType 字典类型
     * @return
     */
    @Override
    public List<SysDictData> getDictDatasByDictType(String dictType) {
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysDictData :: getDictType, dictType);
        return this.list(queryWrapper);
    }
}
