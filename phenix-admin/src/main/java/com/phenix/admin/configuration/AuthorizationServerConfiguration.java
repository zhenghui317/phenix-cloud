package com.phenix.admin.configuration;

import com.phenix.admin.oauth.ClientDetailsServiceImpl;
import com.phenix.admin.oauth.UserDetailsServiceImpl;
import com.phenix.core.exception.PhenixOAuth2WebResponseExceptionTranslator;
import com.phenix.core.security.PhenixOAuthHelper;
import com.phenix.core.security.token.enhancer.PhenixTokenEnhancer;
import com.phenix.core.security.token.store.PhenixRedisTokenStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;

/**
 * 平台认证服务器配置
 *
 * @author liuyadu
 */
@Order
@Slf4j
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PhenixRedisTokenStore phenixRedisTokenStore;
    /**
     * 自定义获取客户端,为了支持自定义权限,
     */
    @Autowired
    private ClientDetailsServiceImpl clientDetailsService;


    /**
     * 授权store
     *
     * @return
     */
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * 令牌信息拓展
     *
     * @return
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new PhenixTokenEnhancer();
    }

    /**
     * 授权码
     *
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .authenticationManager(authenticationManager)
                .approvalStore(approvalStore())
                .userDetailsService(userDetailsService)
                .tokenServices(createDefaultTokenServices())
                .accessTokenConverter(PhenixOAuthHelper.buildAccessTokenConverter())
                .authorizationCodeServices(authorizationCodeServices());
        // 自定义确认授权页面
        endpoints.pathMapping("/oauth/confirm_access", "/oauth/confirm_access");
        // 自定义错误页
        endpoints.pathMapping("/oauth/error", "/oauth/error");
        // 自定义异常转换类
        endpoints.exceptionTranslator(new PhenixOAuth2WebResponseExceptionTranslator());
    }

    private DefaultTokenServices createDefaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(phenixRedisTokenStore);
        tokenServices.setTokenEnhancer(tokenEnhancer());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(true);
        tokenServices.setClientDetailsService(clientDetailsService);
        return tokenServices;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // 开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("isAuthenticated()")
                // 开启表单认证
                .allowFormAuthenticationForClients();
    }

}
