package com.phenix.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phenix.admin.entity.SysAccount;
import com.phenix.admin.entity.AccountLogs;

/**
 * 系统用户登录账号管理
 * 支持多账号登陆
 *
 * @author zhenghui
 */
public interface ISysAccountService extends IService<SysAccount> {


    /**
     * 根据主键获取账号信息
     *
     * @param accountId
     * @return
     */
    SysAccount getAccountById(Long accountId);


    /**
     * 获取账号信息
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    SysAccount getAccount(String account, String accountType, String domain);


    /**
     * 注册账号
     *
     * @param userId
     * @param account
     * @param password
     * @param accountType
     * @param status
     * @param domain
     * @param registerIp
     * @return
     */
    SysAccount register(Long userId, String account, String password, String accountType, Integer status, String domain, String registerIp);


    /**
     * 检查账号是否存在
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    Boolean isExist(String account, String accountType, String domain);


    /**
     * 删除账号
     *
     * @param accountId
     * @return
     */
    int removeAccount(Long accountId);

    /**
     * 更新账号状态
     *
     * @param accountId
     * @param status
     */
    int updateStatus(Long accountId, Integer status);

    /**
     * 根据用户更新账户状态
     *
     * @param userId
     * @param domain
     * @param status
     */
    int updateStatusByUserId(Long userId, String domain, Integer status);

    /**
     * 重置用户密码
     *
     * @param userId
     * @param domain
     * @param password
     */
    int updatePasswordByUserId(Long userId, String domain, String password);

    /**
     * 根据用户ID删除账号
     *
     * @param userId
     * @param domain
     * @return
     */
    int removeAccountByUserId(Long userId, String domain);

    /**
     * 添加登录日志
     *
     * @param log
     */
    void addLoginLog(AccountLogs log);
}
