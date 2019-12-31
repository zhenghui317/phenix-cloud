package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.OauthClientDetails;
import com.phenix.admin.mapper.OauthClientDetailsMapper;
import com.phenix.admin.service.IOauthClientDetailsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * oauth2客户端信息 服务实现类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-13
 */
@Service
public class OauthClientDetailsServiceImpl extends ServiceImpl<OauthClientDetailsMapper, OauthClientDetails> implements IOauthClientDetailsService {

}
