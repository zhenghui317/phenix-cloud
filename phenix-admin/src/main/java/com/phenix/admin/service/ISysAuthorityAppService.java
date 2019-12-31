package com.phenix.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phenix.admin.entity.SysAuthorityApp;
import com.phenix.core.security.authority.PhenixAuthority;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统权限-应用关联 服务类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-17
 */
public interface ISysAuthorityAppService extends IService<SysAuthorityApp> {

    /**
     * 获取应用已授权权限
     *
     * @param appId
     * @return
     */
    List<PhenixAuthority> selectAuthorityByApp(@Param("appId") Long appId);
}
