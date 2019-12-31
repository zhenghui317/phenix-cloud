package com.phenix.core.security;

import com.phenix.core.security.configuration.PhenixCommonProperties;
import com.phenix.core.security.configuration.PhenixOAuthAdminProperties;
import com.phenix.core.utils.BeanConvertUtils;
import com.phenix.tools.reflect.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * 认证信息帮助类
 *
 * @author liuyadu
 */
@Slf4j
public class PhenixOAuthHelper {

    @Autowired
    private DataSource dataSource;

    /**
     * 获取认证用户信息
     *
     * @return
     */
    public static PhenixUserDetails getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication instanceof OAuth2Authentication) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
            OAuth2Request clientToken = oAuth2Authentication.getOAuth2Request();
            if (!oAuth2Authentication.isClientOnly()) {
                if (authentication.getPrincipal() instanceof PhenixUserDetails) {
                    return (PhenixUserDetails) authentication.getPrincipal();
                }
                if (authentication.getPrincipal() instanceof Map) {
                    return BeanConvertUtils.mapToObject((Map) authentication.getPrincipal(), PhenixUserDetails.class);
                }
            } else {
                PhenixUserDetails openUser = new PhenixUserDetails();
                openUser.setClientId(clientToken.getClientId());
                openUser.setAuthorities(clientToken.getAuthorities());
                return openUser;
            }
        }
        return null;
    }


    /**
     * 更新PhenixUser
     *
     * @param openUser
     */
    public static void updatePhenixUser(TokenStore tokenStore, PhenixUserDetails openUser) {
        if (openUser == null) {
            return;
        }
        Assert.notNull(openUser.getClientId(), "客户端ID不能为空");
        Assert.notNull(openUser.getUsername(), "用户名不能为空");
        // 动态更新客户端生成的token
        Collection<OAuth2AccessToken> accessTokens = tokenStore.findTokensByClientIdAndUserName(openUser.getClientId(), openUser.getUsername());
        if (accessTokens != null && !accessTokens.isEmpty()) {
            for (OAuth2AccessToken accessToken : accessTokens) {
                // 由于没有set方法,使用反射机制强制赋值
                OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(accessToken);
                if (oAuth2Authentication != null) {
                    Authentication authentication = oAuth2Authentication.getUserAuthentication();
                    ReflectUtils.setFieldValue(authentication, "principal", openUser);
                    // 重新保存
                    tokenStore.storeAccessToken(accessToken, oAuth2Authentication);
                }
            }
        }
    }


    /***
     * 更新客户端权限
     * @param tokenStore
     * @param clientId
     * @param authorities
     */
    public static void updatePhenixClientAuthorities(TokenStore tokenStore, String clientId, Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null) {
            return;
        }
        // 动态更新客户端生成的token
        Collection<OAuth2AccessToken> accessTokens = tokenStore.findTokensByClientId(clientId);
        if (accessTokens != null && !accessTokens.isEmpty()) {
            Iterator<OAuth2AccessToken> iterator = accessTokens.iterator();
            while (iterator.hasNext()) {
                OAuth2AccessToken token = iterator.next();
                OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(token);
                if (oAuth2Authentication != null && oAuth2Authentication.isClientOnly()) {
                    // 只更新客户端权限
                    // 由于没有set方法,使用反射机制强制赋值
                    ReflectUtils.setFieldValue(oAuth2Authentication, "authorities", authorities);
                    // 重新保存
                    tokenStore.storeAccessToken(token, oAuth2Authentication);
                }
            }
        }
    }


    /**
     * 获取认证用户Id
     *
     * @return
     */
    public static Long getUserId() {
        return getUser().getUserId();
    }

    /**
     * 是否拥有权限
     *
     * @param authority
     * @return
     */
    public static Boolean hasAuthority(String authority) {
        PhenixUserDetails auth = getUser();
        if (auth == null) {
            return false;
        }
        if (AuthorityUtils.authorityListToSet(auth.getAuthorities()).contains(authority)) {
            return true;
        }
        return false;
    }

    /**
     * 构建token转换器
     *
     * @return
     */
    public static DefaultAccessTokenConverter buildAccessTokenConverter() {
        PhenixUserConverter userAuthenticationConverter = new PhenixUserConverter();
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
        return accessTokenConverter;
    }

    /**
     * 构建自定义远程Token服务类
     *
     * @return
     */
    public static RemoteTokenServices buildRemoteTokenServices(PhenixOAuthAdminProperties oAuthAdminProperties) {
        // 使用自定义系统用户凭证转换器
        DefaultAccessTokenConverter accessTokenConverter = buildAccessTokenConverter();
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setCheckTokenEndpointUrl(oAuthAdminProperties.getAccessTokenUri());
        tokenServices.setClientId(oAuthAdminProperties.getClientId());
        tokenServices.setClientSecret(oAuthAdminProperties.getClientSecret());
        tokenServices.setAccessTokenConverter(accessTokenConverter);
        log.info("buildRemoteTokenServices[{}]", tokenServices);
        return tokenServices;
    }

//    /**
//     * 构建资源服务器RedisToken服务类
//     *
//     * @return
//     */
//    public static ResourceServerTokenServices buildRedisTokenServices(RedisConnectionFactory redisConnectionFactory) throws Exception {
//        PhenixRedisTokenService tokenServices = new PhenixRedisTokenService();
//        // 这里的签名key 保持和认证中心一致
//        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
//        tokenServices.setTokenStore(redisTokenStore);
//        log.info("buildRedisTokenServices[{}]", tokenServices);
//        return tokenServices;
//    }

//    /**
//     * 构建资源服务器RedisToken服务类
//     *
//     * @return
//     */
//    public static ResourceServerTokenServices buildJdbcTokenServices(DataSource dataSource) throws Exception {
//
//        PhenixRedisTokenService tokenServices = new PhenixRedisTokenService();
//        // 这里的签名key 保持和认证中心一致
//        JdbcTokenStore jdbcTokenStore = new JdbcTokenStore(dataSource);
//        tokenServices.setTokenStore(jdbcTokenStore);
//        log.info("buildJdbcTokenServices[{}]", tokenServices);
//        return tokenServices;
//    }
}
