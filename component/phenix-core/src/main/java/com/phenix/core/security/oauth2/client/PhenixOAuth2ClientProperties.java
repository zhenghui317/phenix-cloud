package com.phenix.core.security.oauth2.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author: zhenghui
 * @date: 2019/12/12
 * @description:
 */
@ConfigurationProperties(prefix = "phenix.oauth2.client")
public class PhenixOAuth2ClientProperties {

    private Map<String, PhenixOAuth2ClientDetails> oauth2;

    public Map<String, PhenixOAuth2ClientDetails> getOauth2() {
        return oauth2;
    }

    public void setOauth2(Map<String, PhenixOAuth2ClientDetails> oauth2) {
        this.oauth2 = oauth2;
    }
}
