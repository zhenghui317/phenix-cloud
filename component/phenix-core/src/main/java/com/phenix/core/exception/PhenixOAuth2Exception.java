package com.phenix.core.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 自定义oauth2异常提示
 * @author zhenghui
 */
@JsonSerialize(using = PhenixOAuth2ExceptionSerializer.class)
public class PhenixOAuth2Exception extends org.springframework.security.oauth2.common.exceptions.OAuth2Exception {
    private static final long serialVersionUID = 4257807899611076101L;

    public PhenixOAuth2Exception(String msg) {
        super(msg);
    }
}