package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.SysAuthorityApp;
import com.phenix.admin.mapper.SysAuthorityAppMapper;
import com.phenix.admin.service.ISysAuthorityAppService;
import com.phenix.core.security.authority.PhenixAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统权限-应用关联 服务实现类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-17
 */
@Service
public class SysAuthorityAppServiceImpl extends ServiceImpl<SysAuthorityAppMapper, SysAuthorityApp> implements ISysAuthorityAppService {

    @Autowired
    private SysAuthorityAppMapper sysAuthorityAppMapper;

    @Override
    public List<PhenixAuthority> selectAuthorityByApp(Long appId) {
        return sysAuthorityAppMapper.selectAuthorityByApp(appId);
    }
}
