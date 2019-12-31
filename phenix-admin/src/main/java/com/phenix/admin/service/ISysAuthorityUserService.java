package com.phenix.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phenix.admin.entity.SysAuthorityUser;
import com.phenix.admin.pojo.dto.AuthoritySysMenuDTO;
import com.phenix.core.security.authority.PhenixAuthority;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统权限-用户关联 服务类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-17
 */
public interface ISysAuthorityUserService extends IService<SysAuthorityUser> {
    /**
     * 获取用户已授权权限
     *
     * @param userId
     * @return
     */
    List<PhenixAuthority> selectAuthorityByUser(@Param("userId") Long userId);

    /**
     * 获取用户已授权权限完整信息
     *
     * @param userId
     * @return
     */
    List<AuthoritySysMenuDTO> selectAuthorityMenuByUser(@Param("userId") Long userId);
}
