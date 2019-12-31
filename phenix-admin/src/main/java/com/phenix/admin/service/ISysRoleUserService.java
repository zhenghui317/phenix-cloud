package com.phenix.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phenix.admin.entity.SysRole;
import com.phenix.admin.entity.SysRoleUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统角色-用户关联 服务类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-17
 */
public interface ISysRoleUserService extends IService<SysRoleUser> {
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
