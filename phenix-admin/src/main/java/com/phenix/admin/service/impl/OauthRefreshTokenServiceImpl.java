package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.OauthRefreshToken;
import com.phenix.admin.mapper.OauthRefreshTokenMapper;
import com.phenix.admin.service.IOauthRefreshTokenService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * oauth2刷新令牌 服务实现类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-13
 */
@Service
public class OauthRefreshTokenServiceImpl extends ServiceImpl<OauthRefreshTokenMapper, OauthRefreshToken> implements IOauthRefreshTokenService {

}
