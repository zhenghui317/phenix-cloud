package com.phenix.admin.service;

import com.phenix.admin.entity.SysDept;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phenix.admin.entity.SysRole;
import com.phenix.admin.pojo.vo.ZTreeVO;

import java.util.List;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-25
 */
public interface ISysDeptService extends IService<SysDept> {

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    List<SysDept> selectDeptList(SysDept dept);
    /**
     * 查询部门管理树
     *
     * @param dept 部门信息
     * @return 所有部门信息
     */
    List<ZTreeVO> selectDeptTree(SysDept dept);

    /**
     * 根据角色ID查询菜单
     *
     * @param role 角色对象
     * @return 菜单列表
     */
    List<ZTreeVO> roleDeptTreeData(SysRole role);

}
