package com.phenix.admin.pojo.dto;

import com.phenix.admin.entity.SysApi;
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
 * API权限
 * @author zhenghui
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AuthorityApi对象", description="")
public class AuthoritySysApiDTO extends SysApi implements Serializable {

    private static final long serialVersionUID = 3474271304324863160L;
    /**
     * 权限ID
     */
    private Long authorityId;

    /**
     * 权限标识
     */
    private String authority;

    /**
     * 前缀
     */
    private String prefix;

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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if(!(obj instanceof AuthoritySysApiDTO)) {
            return false;
        }
        AuthoritySysApiDTO a = (AuthoritySysApiDTO) obj;
        return this.authorityId.equals(a.getAuthorityId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorityId);
    }
}
