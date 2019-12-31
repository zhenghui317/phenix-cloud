package com.phenix.admin.service;

import com.phenix.admin.entity.SysDictData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phenix.admin.entity.SysDictType;

import java.util.List;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-25
 */
public interface ISysDictDataService extends IService<SysDictData> {

    /**
     * 根据字典编码查询
     * @param dictType 字典类型
     * @return
     */
    List<SysDictData> getDictDatasByDictType(String dictType);
}
