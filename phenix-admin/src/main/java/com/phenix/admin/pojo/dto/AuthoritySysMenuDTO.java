package com.phenix.admin.pojo.dto;

import com.phenix.admin.entity.SysMenu;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Objects;

/**
 * 菜单权限
 * @author zhenghui
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="AuthorityMenu对象", description="")
public class AuthoritySysMenuDTO extends SysMenu {

    private static final long serialVersionUID = 3474271304324863160L;
    /**
     * 权限ID
     */
    private Long authorityId;

    /**
     * 权限标识
     */
    private String authority;


    private List<AuthoritySysActionDTO> actionList;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if(!(obj instanceof AuthoritySysMenuDTO)) {
            return false;
        }
        AuthoritySysMenuDTO a = (AuthoritySysMenuDTO) obj;
        return this.authorityId.equals(a.getAuthorityId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorityId);
    }
}
