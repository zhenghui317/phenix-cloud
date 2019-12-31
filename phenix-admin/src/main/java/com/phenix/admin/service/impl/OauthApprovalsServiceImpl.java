package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.OauthApprovals;
import com.phenix.admin.mapper.OauthApprovalsMapper;
import com.phenix.admin.service.IOauthApprovalsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * oauth2已授权客户端 服务实现类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-13
 */
@Service
public class OauthApprovalsServiceImpl extends ServiceImpl<OauthApprovalsMapper, OauthApprovals> implements IOauthApprovalsService {

}
