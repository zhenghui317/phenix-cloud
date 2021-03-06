package com.phenix.core.security.http;

import com.phenix.core.security.configuration.PhenixCommonProperties;
import com.phenix.core.security.configuration.PhenixOAuthAdminProperties;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 自定义RestTemplate请求工具类
 *
 * @author: liuyadu
 * @date: 2018/12/11 15:51
 * @description:
 */
@Slf4j
@Service
public class PhenixRestTemplate extends RestTemplate {

    @Autowired
    private PhenixOAuthAdminProperties oAuthAdminProperties;

    private ApplicationEventPublisher publisher;

    public PhenixRestTemplate() {
    }
    /**
     * 构建网关Oauth2 client_credentials方式请求
     *
     * @return
     */
    public OAuth2RestTemplate buildOAuth2ClientRequest() {
        return buildOAuth2ClientRequest(oAuthAdminProperties.getClientId(), oAuthAdminProperties.getClientSecret(), oAuthAdminProperties.getAccessTokenUri());
    }

    /**
     * 构建网关Oauth2 client_credentials方式请求
     *
     * @param clientId
     * @param clientSecret
     * @param accessTokenUri
     * @return
     */
    public OAuth2RestTemplate buildOAuth2ClientRequest(String clientId, String clientSecret, String accessTokenUri) {
        ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
        resource.setClientId(clientId);
        resource.setClientSecret(clientSecret);
        resource.setAccessTokenUri(accessTokenUri);
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resource);
        return restTemplate;
    }

    /**
     * 构建网关Oauth2 password方式请求
     *
     * @return
     */
    public OAuth2RestTemplate buildOAuth2PasswordRequest(String username, String password) {
        return buildOAuth2PasswordRequest(oAuthAdminProperties.getClientId(), oAuthAdminProperties.getClientSecret(), oAuthAdminProperties.getAccessTokenUri(), username, password);
    }

    /**
     * 构建网关Oauth2 password方式请求
     *
     * @param clientId
     * @param clientSecret
     * @param accessTokenUri
     * @param username
     * @param password
     * @return
     */
    public OAuth2RestTemplate buildOAuth2PasswordRequest(String clientId, String clientSecret, String accessTokenUri, String username, String password) {
        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
        resource.setUsername(username);
        resource.setPassword(password);
        resource.setClientId(clientId);
        resource.setClientSecret(clientSecret);
        resource.setAccessTokenUri(accessTokenUri);
        resource.setAuthenticationScheme(AuthenticationScheme.form);
        resource.setGrantType("password");
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resource);
        return restTemplate;
    }
    /**
     * 刷新网关
     * 注:不要频繁调用!
     * 1.资源权限发生变化时可以调用
     * 2.流量限制变化时可以调用
     * 3.IP访问发生变化时可以调用
     * 4.智能路由发生变化时可以调用
     */
    public void refreshGateway() {
        try {
            // TODO  刷新网关事件
        } catch (Exception e) {
            log.error("refreshGateway error:{}", e.getMessage());
        }
    }
}
