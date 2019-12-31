package com.phenix.core.security.token.enhancer;

import com.phenix.core.security.PhenixUserDetails;
import com.phenix.core.security.constants.PhenixTokenConstants;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 自定义JwtAccessToken转换器
 * @author zhenghui
 * @date: 2019/12/12
 */
public class PhenixTokenEnhancer extends TokenEnhancerChain {

    /**
     * 生成token
     *
     * @param accessToken
     * @param authentication
     * @return
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);
        final Map<String, Object> additionalInfo = new HashMap<>(8);
        if (!authentication.isClientOnly()) {
            if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof PhenixUserDetails) {
                // 设置额外用户信息
                PhenixUserDetails baseUser = ((PhenixUserDetails) authentication.getPrincipal());
                additionalInfo.put(PhenixTokenConstants.USER_ID, baseUser.getUserId().toString());
                additionalInfo.put(PhenixTokenConstants.TENANT_ID, baseUser.getTenantId().toString());
                additionalInfo.put(PhenixTokenConstants.DOMAIN, baseUser.getDomain());
                additionalInfo.put(PhenixTokenConstants.USER_NAME, baseUser.getUsername());
                additionalInfo.put(PhenixTokenConstants.AVATAR, baseUser.getAvatar());
            }
        }
        defaultOAuth2AccessToken.setAdditionalInformation(additionalInfo);
        return super.enhance(defaultOAuth2AccessToken, authentication);
    }
}
