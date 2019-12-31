package com.phenix.admin.pojo.dto;

import com.phenix.admin.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户角色DTO
 *
 * @author zhenghui
 * @date  2019-12-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserRoleDTO extends SysUser {
    List<Long> roles;
}
