package com.phenix.core.security.authority;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * 自定义已授权角色标识
 * @author zhenghui
 * @date: 2019/12/22
 */
public class PhenixRole implements GrantedAuthority {
    /**
     * 角色id
     */
    private String roleId;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

    @Override
    public String getAuthority() {
        return this.roleCode;
    }


    public PhenixRole() {
    }

    public PhenixRole(String roleCode) {
        Assert.hasText(roleCode, "A granted roleCode textual representation is required");
        this.roleCode = roleCode;
    }

    public PhenixRole(String roleId, String roleCode, String roleName) {
        this.roleId = roleId;
        this.roleCode = roleCode;
        this.roleName = roleName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }


    @Override
    public int hashCode() {
        return this.roleId.hashCode();
    }

    @Override
    public String toString() {
        return this.roleId;
    }
}
