package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.OauthClientToken;
import com.phenix.admin.mapper.OauthClientTokenMapper;
import com.phenix.admin.service.IOauthClientTokenService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * oauth2客户端令牌 服务实现类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-13
 */
@Service
public class OauthClientTokenServiceImpl extends ServiceImpl<OauthClientTokenMapper, OauthClientToken> implements IOauthClientTokenService {

}
