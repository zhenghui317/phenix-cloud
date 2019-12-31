package com.phenix.admin.mapper;

import com.phenix.admin.entity.SysDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-25
 */
public interface SysDeptMapper extends BaseMapper<SysDept> {

    /**
     * 查询部门管理数据
     *
     * @param parentId 上级id
     * @param deptName 部门名称
     * @param status   状态
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptList(@Param("parentId") Long parentId, @Param("deptName") String deptName, @Param("status") Integer status);


    /**
     * 根据角色ID查询部门
     *
     * @param roleId 角色ID
     * @return 部门列表
     */
    public List<String> selectRoleDeptTree(@Param("roleId") Long roleId);
}
