package com.phenix.core.security;

import com.phenix.core.security.constants.PhenixTokenConstants;
import com.phenix.core.utils.BeanConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 自定义认证用户信息转换器
 *
 * @author zhenghui
 * @date: 2019/12/12
 */
@Slf4j
public class PhenixUserConverter extends DefaultUserAuthenticationConverter {


    public PhenixUserConverter() {
    }

    /**
     * 转换为自定义信息
     *
     * @param map
     * @return
     */
    private Object converter(Map<String, ?> map) {
        Map<String, Object> params = new HashMap<String, Object>();
        for (String key : map.keySet()) {
            if (USERNAME.equals(key)) {
                if (map.get(key) instanceof Map) {
                    params.putAll((Map) map.get(key));
                }
                else  if (map.get(key) instanceof PhenixUserDetails) {
                   return map.get(key);
                }else {
                    params.put(key, map.get(key));
                }
            } else {
                params.put(key, map.get(key));
            }
        }
        PhenixUserDetails auth = BeanConvertUtils.mapToObject(params, PhenixUserDetails.class);
        if (params.get(USERNAME) != null) {
            auth.setUserName(params.get(USERNAME).toString());
        }
        if (params.get(PhenixTokenConstants.OPEN_ID) != null) {
            auth.setUserId(Long.parseLong(params.get(PhenixTokenConstants.OPEN_ID).toString()));
        }
        if (params.get(PhenixTokenConstants.DOMAIN) != null) {
            auth.setDomain(params.get(PhenixTokenConstants.DOMAIN).toString());
        }
        auth.setClientId(params.get(AccessTokenConverter.CLIENT_ID).toString());
        auth.setAuthorities(getAuthorities(map));
        return auth;
    }

    /**
     * 转换用户
     *
     * @param authentication
     * @return
     */
    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap();
        response.put(USERNAME, authentication.getPrincipal());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return response;
    }

    /**
     * 读取认证信息
     *
     * @param map
     * @return
     */
    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Object principal = converter(map);
            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
            if (principal != null) {
                PhenixUserDetails user = (PhenixUserDetails) principal;
                authorities = user.getAuthorities();
            }
            return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
        }
        return null;
    }

    /**
     * 获取权限
     *
     * @param map
     * @return
     */
    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        if (!map.containsKey(AUTHORITIES)) {
            return AuthorityUtils.NO_AUTHORITIES;
        }
        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
                    .collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }

}
