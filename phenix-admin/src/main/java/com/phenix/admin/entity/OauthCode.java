package com.phenix.admin.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Blob;

/**
 * <p>
    * oauth2授权码
    * </p>
 *
 * @author zhenghui
 * @since 2019-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OauthCode对象", description="oauth2授权码")
public class OauthCode implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private Blob authentication;


}
