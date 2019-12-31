package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.SysAuthorityUser;
import com.phenix.admin.mapper.SysAuthorityUserMapper;
import com.phenix.admin.pojo.dto.AuthoritySysMenuDTO;
import com.phenix.admin.service.ISysAuthorityUserService;
import com.phenix.core.security.authority.PhenixAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统权限-用户关联 服务实现类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-17
 */
@Service
public class SysAuthorityUserServiceImpl extends ServiceImpl<SysAuthorityUserMapper, SysAuthorityUser> implements ISysAuthorityUserService {

    @Autowired
    private SysAuthorityUserMapper sysAuthorityUserMapper;

    @Override
    public List<PhenixAuthority> selectAuthorityByUser(Long userId) {
        return sysAuthorityUserMapper.selectAuthorityByUser(userId);
    }

    @Override
    public List<AuthoritySysMenuDTO> selectAuthorityMenuByUser(Long userId) {
        return sysAuthorityUserMapper.selectAuthorityMenuByUser(userId);
    }
}
