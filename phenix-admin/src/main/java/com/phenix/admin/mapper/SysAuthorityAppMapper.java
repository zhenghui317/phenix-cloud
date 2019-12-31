package com.phenix.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phenix.admin.entity.SysAuthorityApp;
import com.phenix.core.security.authority.PhenixAuthority;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhenghui
 */
public interface SysAuthorityAppMapper extends BaseMapper<SysAuthorityApp> {

    /**
     * 获取应用已授权权限
     *
     * @param appId
     * @return
     */
    List<PhenixAuthority> selectAuthorityByApp(@Param("appId") Long appId);
}
