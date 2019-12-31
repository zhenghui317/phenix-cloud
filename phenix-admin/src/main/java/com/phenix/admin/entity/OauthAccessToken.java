package com.phenix.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Blob;

/**
 * <p>
    * oauth2访问令牌
    * </p>
 *
 * @author zhenghui
 * @since 2019-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oauth_access_token")
@ApiModel(value="OauthAccessToken对象", description="oauth2访问令牌")
public class OauthAccessToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tokenId;

    private Blob token;

    @TableId(value = "authentication_id", type = IdType.ASSIGN_ID)
    private String authenticationId;

    private String userName;

    private String clientId;

    private Blob authentication;

    private String refreshToken;


}
