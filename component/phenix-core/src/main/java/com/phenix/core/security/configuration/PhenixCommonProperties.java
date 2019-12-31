package com.phenix.core.security.configuration;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "phenix.oauth2.common")
public class PhenixCommonProperties {
    /**
     *
     */
    private String adminServerAddr;
    /**
     *
     */
    private String apiServerAddr;
    /**
     *
     */
    private String authServerAddr;
    /**
     *
     */
    private String userAuthorizationUri;
    /**
     *
     */
    private String accessTokenUri;
    /**
     *
     */
    private String tokenInfoUri;
    /**
     *
     */
    private String userInfoUri;
}
