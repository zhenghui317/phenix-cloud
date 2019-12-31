package com.phenix.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phenix.admin.entity.SysAuthorityUser;
import com.phenix.admin.pojo.dto.AuthoritySysMenuDTO;
import com.phenix.core.security.authority.PhenixAuthority;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhenghui
 */
public interface SysAuthorityUserMapper extends BaseMapper<SysAuthorityUser> {

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
