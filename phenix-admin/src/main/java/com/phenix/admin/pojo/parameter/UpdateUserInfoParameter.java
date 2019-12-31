package com.phenix.admin.pojo.parameter;

import lombok.Getter;

/**
 * 修改当前登录用户基本信息参数
 *
 * @author zhenghui
 * @date 2019-12-20
 */
@Getter
public class UpdateUserInfoParameter {

    private String nickName;
    private String userDesc;
    private String avatar;
}
