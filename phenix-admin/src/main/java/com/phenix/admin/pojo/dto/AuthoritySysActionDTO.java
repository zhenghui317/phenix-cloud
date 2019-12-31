package com.phenix.admin.pojo.dto;

import com.phenix.admin.entity.SysAction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

/**
 * 功能权限
 * @author zhenghui
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="AuthorityAction对象", description="")
public class AuthoritySysActionDTO extends SysAction implements Serializable {
    private static final long serialVersionUID = -691740581827186502L;

    /**
     * 权限ID
     */
    private Long authorityId;

    /**
     * 权限标识
     */
    private String authority;


    /**
     * 是否需要安全认证
     */
    private Boolean isAuth = true;

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Boolean getAuth() {
        return isAuth;
    }

    public void setAuth(Boolean auth) {
        isAuth = auth;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if(!(obj instanceof AuthoritySysActionDTO)) {
            return false;
        }
        AuthoritySysActionDTO a = (AuthoritySysActionDTO) obj;
        return this.authorityId.equals(a.getAuthorityId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorityId);
    }
}
