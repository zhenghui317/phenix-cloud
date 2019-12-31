package com.phenix.admin.pojo.parameter;

import lombok.Getter;

/**
 * 更新密码接口参数
 * @author zhenghui
 * @date 2019-12-20
 */
@Getter
public class UpdatePasswordParameter {
    private Long userId;
    private String password;
}
