package com.phenix.admin.pojo.parameter;

import lombok.Data;

/**
 * 登录参数
 */
@Data
public class LoginParameter {

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 登录类型，默认password
     */
    private String logintype;
}
