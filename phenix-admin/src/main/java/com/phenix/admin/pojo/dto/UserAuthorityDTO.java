package com.phenix.admin.pojo.dto;

import com.google.common.collect.Lists;
import com.phenix.core.security.authority.PhenixAuthority;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author: zhenghui
 * @date: 2018/11/12 11:35
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="UserAccount对象", description="")
public class UserAuthorityDTO implements Serializable {
    private static final long serialVersionUID = 6717800085953996702L;

    private Collection<Map> roles = Lists.newArrayList();
    /**
     * 用户权限
     */
    private Collection<PhenixAuthority> authorities = Lists.newArrayList();

}
