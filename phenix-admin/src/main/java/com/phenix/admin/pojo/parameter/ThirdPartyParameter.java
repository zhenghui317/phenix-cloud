package com.phenix.admin.pojo.parameter;

import lombok.Getter;

/**
 * 添加第三方帐号参数
 *
 * @author zhenghui
 * @date 2019-12-20
 */
@Getter
public class ThirdPartyParameter {
    private String account;
    private String password;
    private String accountType;
    private String nickName;
    private String avatar;
}
