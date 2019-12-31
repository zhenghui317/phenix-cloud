package com.phenix.admin.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Blob;

/**
 * <p>
    * oauth2刷新令牌
    * </p>
 *
 * @author zhenghui
 * @since 2019-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OauthRefreshToken对象", description="oauth2刷新令牌")
public class OauthRefreshToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tokenId;

    private Blob token;

    private Blob authentication;


}
