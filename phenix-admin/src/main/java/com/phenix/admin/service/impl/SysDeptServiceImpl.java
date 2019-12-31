package com.phenix.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.phenix.admin.entity.SysDept;
import com.phenix.admin.entity.SysRole;
import com.phenix.admin.mapper.SysDeptMapper;
import com.phenix.admin.pojo.vo.ZTreeVO;
import com.phenix.admin.service.ISysDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.defines.constants.BaseConstants;
import com.phenix.tools.string.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-25
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Autowired
    private SysDeptMapper deptMapper;

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    public List<SysDept> selectDeptList(SysDept dept) {
        return deptMapper.selectDeptList(dept.getParentId(), dept.getDeptName(), dept.getStatus());
    }


    @Override
    public List<ZTreeVO> selectDeptTree(SysDept dept) {
        List<SysDept> deptList = deptMapper.selectDeptList(dept.getParentId(), dept.getDeptName(), dept.getStatus());
        List<ZTreeVO> ztrees = initZtree(deptList);
        return ztrees;
    }


    /**
     * 根据角色ID查询部门（数据权限）
     *
     * @param role 角色对象
     * @return 部门列表（数据权限）
     */
    @Override
    public List<ZTreeVO> roleDeptTreeData(SysRole role) {
        Long roleId = role.getRoleId();
        List<ZTreeVO> ztrees = new ArrayList<ZTreeVO>();
        List<SysDept> deptList = selectDeptList(new SysDept());
        if (roleId != null) {
            List<String> roleDeptList = deptMapper.selectRoleDeptTree(roleId);
            ztrees = initZtree(deptList, roleDeptList);
        } else {
            ztrees = initZtree(deptList);
        }
        return ztrees;
    }


    /**
     * 对象转部门树
     *
     * @param deptList 部门列表
     * @return 树结构列表
     */
    public List<ZTreeVO> initZtree(List<SysDept> deptList) {
        return initZtree(deptList, null);
    }

    /**
     * 对象转部门树
     *
     * @param deptList     部门列表
     * @param roleDeptList 角色已存在菜单列表
     * @return 树结构列表
     */
    public List<ZTreeVO> initZtree(List<SysDept> deptList, List<String> roleDeptList) {

        List<ZTreeVO> ztrees = new ArrayList<ZTreeVO>();
        boolean isCheck = CollectionUtil.isNotEmpty(roleDeptList);
        for (SysDept dept : deptList) {
            if (BaseConstants.ENABLED.equals(dept.getStatus())) {
                ZTreeVO ztree = new ZTreeVO();
                ztree.setId(dept.getDeptId());
                ztree.setpId(dept.getParentId());
                ztree.setName(dept.getDeptName());
                ztree.setTitle(dept.getDeptName());
                if (isCheck) {
                    ztree.setChecked(roleDeptList.contains(dept.getDeptId() + dept.getDeptName()));
                }
                ztrees.add(ztree);
            }
        }
        return ztrees;
    }

}
