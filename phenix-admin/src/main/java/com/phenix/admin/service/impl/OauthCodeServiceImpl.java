package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.OauthCode;
import com.phenix.admin.mapper.OauthCodeMapper;
import com.phenix.admin.service.IOauthCodeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * oauth2授权码 服务实现类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-13
 */
@Service
public class OauthCodeServiceImpl extends ServiceImpl<OauthCodeMapper, OauthCode> implements IOauthCodeService {

}
