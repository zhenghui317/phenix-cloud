package com.phenix.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.phenix.admin.pojo.parameter.LoginParameter;
import com.phenix.core.security.PhenixOAuthHelper;
import com.phenix.core.security.configuration.PhenixOAuthAdminProperties;
import com.phenix.core.security.constants.AuthorizationGrantType;
import com.phenix.core.utils.WebUtils;
import com.phenix.defines.response.ResponseMessage;
import com.phenix.tools.http.HttpClientUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2018/11/9 15:43
 * @description:
 */
@Api(tags = "用户认证中心")
@RestController
public class LoginController {
    @Autowired
    private PhenixOAuthAdminProperties oAuthAdminProperties;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private RestTemplate restTemplate;



    /**
     * 获取用户访问令牌
     * 基于oauth2密码模式登录
     *
     * @param loginParameter
     * @return access_token
     */
    @ApiOperation(value = "登录获取用户访问令牌", notes = "基于oauth2密码模式登录,无需签名,返回access_token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", required = true, value = "登录名", paramType = "form"),
            @ApiImplicitParam(name = "password", required = true, value = "登录密码", paramType = "form")
    })
    @PostMapping("/user/login")
    public Object getLoginToken(LoginParameter loginParameter, @RequestHeader HttpHeaders httpHeaders) throws Exception {
        Map result = getToken(loginParameter.getUsername(), loginParameter.getPassword(), null,httpHeaders);
        if (result.containsKey("access_token")) {
            return ResponseMessage.ok(result);
        } else {
            return result;
        }
    }

    /**
     * 退出移除令牌
     *
     * @param token
     */
    @ApiOperation(value = "退出并移除令牌", notes = "退出并移除令牌,令牌将失效")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", required = true, value = "访问令牌", paramType = "form")
    })
    @PostMapping("/user/logout")
    public ResponseMessage removeToken(@RequestParam String token) {
        tokenStore.removeAccessToken(tokenStore.readAccessToken(token));
        return ResponseMessage.ok();
    }


    public JSONObject getToken(String userName, String password, String type, HttpHeaders headers) {
        String url = WebUtils.getServerUrl(WebUtils.getHttpServletRequest()) + "/oauth/token";
        // 使用oauth2密码模式登录.
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("username", userName);
        postParameters.add("password", password);
        postParameters.add("client_id", oAuthAdminProperties.getClientId());
        postParameters.add("client_secret", oAuthAdminProperties.getClientSecret());
        postParameters.add("grant_type", AuthorizationGrantType.PASSWORD);
        // 添加参数区分,第三方登录
        postParameters.add("login_type", type);
        // 使用客户端的请求头,发起请求
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 强制移除 原来的请求头,防止token失效
        headers.remove(HttpHeaders.AUTHORIZATION);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity(postParameters, headers);
        JSONObject result = restTemplate.postForObject(url, request, JSONObject.class);
        return result;
    }
}
