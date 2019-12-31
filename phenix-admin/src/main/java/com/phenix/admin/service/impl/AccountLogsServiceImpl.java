package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.AccountLogs;
import com.phenix.admin.mapper.AccountLogsMapper;
import com.phenix.admin.service.IAccountLogsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录日志 服务实现类
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-16
 */
@Service
public class AccountLogsServiceImpl extends ServiceImpl<AccountLogsMapper, AccountLogs> implements IAccountLogsService {

}
