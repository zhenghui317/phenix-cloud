package com.phenix.admin.pojo.parameter;

import lombok.Getter;

import java.util.List;

/**
 *
 * 添加角色用户集合参数
 * @author zhenghui
 * @date 2019-12-20
 */
@Getter
public class RoleUsersParameter {
    private Long roleId;
    private List<Long> userIds;
}
