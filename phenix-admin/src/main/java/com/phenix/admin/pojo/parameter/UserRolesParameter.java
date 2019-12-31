package com.phenix.admin.pojo.parameter;

import lombok.Getter;

import java.util.List;

/**
 * 添加用户权限集合参数
 * @author zhenghui
 * @date 2019-12-20
 */
@Getter
public class UserRolesParameter {
    private Long userId;
    private List<Long> roleIds;
}
