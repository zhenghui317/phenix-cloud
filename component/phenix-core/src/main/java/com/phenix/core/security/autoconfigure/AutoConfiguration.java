package com.phenix.core.security.autoconfigure;

import com.phenix.core.exception.PhenixGlobalExceptionHandler;
import com.phenix.core.exception.PhenixRestResponseErrorHandler;
import com.phenix.core.security.configuration.PhenixCommonProperties;
import com.phenix.core.security.configuration.PhenixOAuthAdminProperties;
import com.phenix.core.security.http.PhenixRestTemplate;
import com.phenix.core.security.oauth2.client.PhenixOAuth2ClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

/**
 * 默认配置类
 *
 * @author liuyadu
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({PhenixCommonProperties.class
        , PhenixOAuth2ClientProperties.class
        , PhenixOAuthAdminProperties.class})
public class AutoConfiguration {

    /**
     * 默认加密配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder bCryptPasswordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        log.info("BCryptPasswordEncoder [{}]", encoder);
        return encoder;
    }


    /**
     * 统一异常处理配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(PhenixGlobalExceptionHandler.class)
    public PhenixGlobalExceptionHandler exceptionHandler() {
        PhenixGlobalExceptionHandler exceptionHandler = new PhenixGlobalExceptionHandler();
        log.info("PhenixGlobalExceptionHandler [{}]", exceptionHandler);
        return exceptionHandler;
    }


    /**
     * 自定义Oauth2请求类
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(PhenixRestTemplate.class)
    public PhenixRestTemplate dingJustRestTemplate() {
        PhenixRestTemplate restTemplate = new PhenixRestTemplate();
        //设置自定义ErrorHandler
        restTemplate.setErrorHandler(new PhenixRestResponseErrorHandler());
        log.info("PhenixRestTemplate [{}]", restTemplate);
        return restTemplate;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        //设置自定义ErrorHandler
        restTemplate.setErrorHandler(new PhenixRestResponseErrorHandler());
        log.info("RestTemplate [{}]", restTemplate);
        return restTemplate;
    }

}
