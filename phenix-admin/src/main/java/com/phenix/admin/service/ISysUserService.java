package com.phenix.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phenix.admin.entity.SysUser;
import com.phenix.admin.pojo.dto.UserSysAccountDTO;

import java.util.List;

/**
 * 系统用户资料管理
 *
 * @author: zhenghui
 * @date: 2018/10/24 16:38
 * @description:
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 添加用户信息
     * @param sysUser
     * @return
     */
    Boolean addUser(SysUser sysUser, String password);

    /**
     * 更新系统用户
     *
     * @param sysUser
     * @return
     */
    Boolean updateUser(SysUser sysUser);

    /**
     * 添加第三方登录用户
     *
     * @param sysUser
     * @param accountType
     * @param
     */
    Boolean addUserThirdParty(SysUser sysUser, String password, String accountType);

    /**
     * 更新密码
     *
     * @param userId
     * @param password
     */
    Boolean updatePassword(Long userId, String password);

    /**
     * 查询列表
     *
     * @return
     */
    List<SysUser> findAllList();


    /**
     * 根据用户ID获取用户信息
     *
     * @param userId
     * @return
     */
    SysUser getUserById(Long userId);

    /**
     * 获取用户权限
     *
     * @param userId
     * @return
     */
    UserSysAccountDTO getUserAccount(Long userId);

    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    SysUser getUserByUsername(String username);


    /**
     * 支持密码、手机号、email登陆
     * 其他方式没有规则，无法自动识别。需要单独开发
     *
     * @param account 登陆账号
     * @return
     */
    UserSysAccountDTO getUserAccountInfo(String account);
}
