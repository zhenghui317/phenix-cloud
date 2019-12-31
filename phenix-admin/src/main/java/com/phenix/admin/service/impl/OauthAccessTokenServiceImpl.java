package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.OauthAccessToken;
import com.phenix.admin.mapper.OauthAccessTokenMapper;
import com.phenix.admin.service.IOauthAccessTokenService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * oauth2访问令牌 服务实现类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-13
 */
@Service
public class OauthAccessTokenServiceImpl extends ServiceImpl<OauthAccessTokenMapper, OauthAccessToken> implements IOauthAccessTokenService {

}
