package com.phenix.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phenix.admin.entity.SysRole;

import java.util.List;
import java.util.Map;

/**
 * @author zhenghui
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> selectRoleList(Map params);
}
