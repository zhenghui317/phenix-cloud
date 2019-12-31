package com.phenix.admin.pojo.dto;

import com.phenix.admin.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户DTO
 * @author zhenghui
 * @date  2019-12-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserDTO extends SysUser {
    /**
     * 密码
     */
    private String password;
}
