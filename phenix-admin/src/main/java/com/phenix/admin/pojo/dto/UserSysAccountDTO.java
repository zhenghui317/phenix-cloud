package com.phenix.admin.pojo.dto;

import com.phenix.admin.entity.SysAccount;
import com.google.common.collect.Lists;
import com.phenix.core.security.authority.PhenixAuthority;
import com.phenix.core.security.authority.PhenixRole;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author: zhenghui
 * @date: 2018/11/12 11:35
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UserAccount对象", description="")
public class UserSysAccountDTO extends SysAccount implements Serializable {
    private static final long serialVersionUID = 6717800085953996702L;

    private Collection<PhenixRole> roles = Lists.newArrayList();
    /**
     * 用户权限
     */
    private Collection<PhenixAuthority> authorities = Lists.newArrayList();
    /**
     * 第三方账号
     */
    private String thirdParty;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

}
