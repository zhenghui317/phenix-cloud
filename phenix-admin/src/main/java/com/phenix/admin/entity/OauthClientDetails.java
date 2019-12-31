package com.phenix.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
    * oauth2客户端信息
    * </p>
 *
 * @author zhenghui
 * @since 2019-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oauth_client_details")
@ApiModel(value="OauthClientDetails对象", description="oauth2客户端信息")
public class OauthClientDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "client_id", type = IdType.ASSIGN_ID)
    private String clientId;

    private String clientSecret;

    private String resourceIds;

    private String scope;

    private String authorizedGrantTypes;

    private String webServerRedirectUri;

    private String authorities;

    private Integer accessTokenValidity;

    private Integer refreshTokenValidity;

    private String additionalInformation;

    private String autoapprove;


}
