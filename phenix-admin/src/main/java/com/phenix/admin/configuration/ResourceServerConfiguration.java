package com.phenix.admin.configuration;

import com.phenix.core.exception.PhenixAccessDeniedHandler;
import com.phenix.core.exception.PhenixAuthenticationEntryPoint;
import com.phenix.core.security.token.service.PhenixRedisTokenService;
import com.phenix.core.security.token.store.PhenixRedisTokenStore;
import com.phenix.starter.mysql.datasource.UmspscDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;

import javax.sql.DataSource;

/**
 * oauth2资源服务器配置
 *
 * @author: zhenghui
 * @date: 2019/12/19
 * @description:
 */
@Slf4j
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private PhenixRedisTokenStore phenixRedisTokenStore;

   // private BearerTokenExtractor tokenExtractor = new BearerTokenExtractor();

    @Primary
    @Bean
    public DataSource dataSource() {
        UmspscDataSource dataSource = new UmspscDataSource();
        return dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder;
    }

    @Bean
    public JdbcClientDetailsService clientDetailsService() {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource());
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder());
        return jdbcClientDetailsService;
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        PhenixRedisTokenService tokenServices = new PhenixRedisTokenService();
        // 这里的签名key 保持和认证中心一致
        tokenServices.setTokenStore(phenixRedisTokenStore);
        log.info("buildRedisTokenServices[{}]", tokenServices);
        // 构建redis获取token服务
        resources.tokenServices(tokenServices);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                // 监控端点内部放行
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                // fegin访问或无需身份认证
                .antMatchers(
                        "/authority/access",
                        "/authority/app",
                        "/app/*/info",
                        "/app/client/*/info",
                        "/feign/**",
                        "/gateway/api/**",
                        "/user/add/thirdParty",
                        "/user/login",
                        "/user/info",
                        "/current/**",
                        "/password/**",
                        "/swagger-ui.html",
                        "/login/**",
                        "/oauth/**",
                        "/password/**",
                        "/static/**",
                        "/",
                        "/index",
                        "/login",
                        "/welcome","/error", "/oauth/confirm_access"
                ).permitAll()
                // 监控端点内部放行
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().permitAll()
                // /logout退出清除cookie
                .addLogoutHandler(new CookieClearingLogoutHandler("token", "remember-me"))
               // .logoutSuccessHandler(new LogoutSuccessHandler())
                .and()
                //认证鉴权错误处理,为了统一异常处理。每个资源服务器都应该加上。
                .exceptionHandling()
                .accessDeniedHandler(new PhenixAccessDeniedHandler())
                .authenticationEntryPoint(new PhenixAuthenticationEntryPoint())
                .and()
                .csrf().disable();
    }


//    public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
//        public LogoutSuccessHandler() {
//            // 重定向到原地址
//            this.setUseReferer(true);
//        }
//
//        @Override
//        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//            try {
//                // 解密请求头
//                authentication =  tokenExtractor.extract(request);
//                if(authentication!=null && authentication.getPrincipal()!=null){
//                    String tokenValue = authentication.getPrincipal().toString();
//                    log.debug("revokeToken tokenValue:{}",tokenValue);
//                    // 移除token
//                    tokenStore.removeAccessToken(tokenStore.readAccessToken(tokenValue));
//                }
//            }catch (Exception e){
//                log.error("revokeToken error:{}",e);
//            }
//            super.onLogoutSuccess(request, response, authentication);
//        }
//    }
}

