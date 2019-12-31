package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.SysTenant;
import com.phenix.admin.mapper.SysTenantMapper;
import com.phenix.admin.service.ISysTenantService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 租户信息表 服务实现类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-17
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

}
