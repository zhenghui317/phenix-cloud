package com.phenix.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phenix.admin.entity.SysRole;
import com.phenix.admin.entity.SysRoleUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhenghui
 */
public interface SysRoleUserMapper extends BaseMapper<SysRoleUser> {
    /**
     * 查询系统用户角色
     *
     * @param userId
     * @return
     */
    List<SysRole> selectRoleUserList(@Param("userId") Long userId);

    /**
     * 查询用户角色ID列表
     * @param userId
     * @return
     */
    List<Long> selectRoleUserIdList(@Param("userId") Long userId);
}
