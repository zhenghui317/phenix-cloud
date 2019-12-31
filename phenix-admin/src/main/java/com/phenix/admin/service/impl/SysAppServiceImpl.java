package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.SysApp;
import com.phenix.admin.mapper.SysAppMapper;
import com.phenix.admin.oauth.PhenixJdbcClientDetailsService;
import com.phenix.admin.service.ISysAppService;
import com.phenix.admin.service.ISysAuthorityService;
import com.phenix.core.exception.PhenixAlertException;
import com.phenix.core.security.PhenixClientDetails;
import com.phenix.core.security.constants.AuthorizationGrantType;
import com.phenix.core.utils.BeanConvertUtils;
import com.phenix.defines.constants.BaseConstants;
import com.phenix.tools.random.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

/**
 * @author: zhenghui
 * @date: 2018/11/12 16:26
 * @description:
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysAppServiceImpl extends ServiceImpl<SysAppMapper, SysApp> implements ISysAppService {

    @Autowired
    private ISysAuthorityService authorityService;
    @Autowired
    private PhenixJdbcClientDetailsService phenixJdbcClientDetailsService;
    /**
     * token有效期，默认12小时
     */
    public static final int ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 12;
    /**
     * token有效期，默认7天
     */
    public static final int REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 7;


    /**
     * 获取app详情
     *
     * @param appId
     * @return
     */
    @Cacheable(value = "apps", key = "#appId")
    @Override
    public SysApp getAppInfo(Long appId) {
        return this.getById(appId);
    }

    /**
     * 获取app和应用信息
     *
     * @return
     */
    @Override
    @Cacheable(value = "apps", key = "'client:'+#clientId")
    public PhenixClientDetails getAppClientInfo(String clientId) {
        ClientDetails baseClientDetails = null;
        try {
            baseClientDetails = (ClientDetails) phenixJdbcClientDetailsService.loadClientByClientId(clientId);
        } catch (Exception e) {
            return null;
        }
        Long appId = Long.parseLong(baseClientDetails.getAdditionalInformation().get("appId").toString());
        PhenixClientDetails clientDetails = new PhenixClientDetails();
        BeanUtils.copyProperties(baseClientDetails, clientDetails);
        clientDetails.setAuthorities(authorityService.findAuthorityByApp(appId));
        return clientDetails;
    }

    /**
     * 更新应用开发新型
     *
     * @param client
     */
    @CacheEvict(value = {"apps"}, key = "'client:'+#client.clientId")
    @Override
    public void updateAppClientInfo(PhenixClientDetails client) {
        phenixJdbcClientDetailsService.updateClientDetails(client);
    }

    /**
     * 添加应用
     *
     * @param sysApp
     * @return 应用信息
     */
    @CachePut(value = "apps", key = "#sysApp.appId")
    @Override
    public SysApp addAppInfo(SysApp sysApp) {
        Long appId = IdWorker.getId();
        String apiKey = RandomUtils.randomAlphaAndNumeric(24);
        String secretKey = RandomUtils.randomAlphaAndNumeric(32);
        sysApp.setAppId(appId);
        sysApp.setApiKey(apiKey);
        sysApp.setSecretKey(secretKey);
        sysApp.setCreateTime(LocalDateTime.now());
        sysApp.setUpdateTime(sysApp.getCreateTime());
        if (sysApp.getIsPersist() == null) {
            sysApp.setIsPersist(BaseConstants.TRUE);
        }
        this.save(sysApp);
        Map info = BeanConvertUtils.objectToMap(sysApp);
        // 功能授权
        PhenixClientDetails client = new PhenixClientDetails();
        client.setClientId(sysApp.getApiKey());
        client.setClientSecret(sysApp.getSecretKey());
        client.setAdditionalInformation(info);
        client.setAuthorizedGrantTypes(Arrays.asList(
                AuthorizationGrantType.AUTHORIZATION_CODE,
                AuthorizationGrantType.CLIENT_CREDENTIALS,
                AuthorizationGrantType.IMPLICIT,
                AuthorizationGrantType.REFRESH_TOKEN));
        client.setAccessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS);
        client.setRefreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS);
        phenixJdbcClientDetailsService.addClientDetails(client);
        return sysApp;
    }

    /**
     * 修改应用
     *
     * @param sysApp 应用
     * @return 应用信息
     */
    @Caching(evict = {
            @CacheEvict(value = {"apps"}, key = "#sysApp.appId"),
            @CacheEvict(value = {"apps"}, key = "'client:'+#sysApp.appId")
    })
    @Override
    public SysApp updateInfo(SysApp sysApp) {
        sysApp.setUpdateTime(LocalDateTime.now());
        this.updateById(sysApp);
        // 修改客户端附加信息
        SysApp sysAppInfo = getAppInfo(sysApp.getAppId());
        Map info = BeanConvertUtils.objectToMap(sysAppInfo);
        PhenixClientDetails client = (PhenixClientDetails) phenixJdbcClientDetailsService.loadClientByClientId(sysAppInfo.getApiKey());
        client.setAdditionalInformation(info);
        phenixJdbcClientDetailsService.updateClientDetails(client);
        return sysApp;
    }

    /**
     * 重置秘钥
     *
     * @param appId
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = {"apps"}, key = "#appId"),
            @CacheEvict(value = {"apps"}, key = "'client:'+#appId")
    })
    public String restSecret(Long appId) {
        SysApp sysAppInfo = getAppInfo(appId);
        if (sysAppInfo == null) {
            throw new PhenixAlertException(appId + "应用不存在!");
        }
        if (BaseConstants.ENABLED.equals(sysAppInfo.getIsPersist())) {
            throw new PhenixAlertException(String.format("保留数据,不允许修改"));
        }
        // 生成新的密钥
        String secretKey = RandomUtils.randomAlpha(32);
        sysAppInfo.setSecretKey(secretKey);
        sysAppInfo.setUpdateTime(LocalDateTime.now());
        this.updateById(sysAppInfo);
        phenixJdbcClientDetailsService.updateClientSecret(sysAppInfo.getApiKey(), secretKey);
        return secretKey;
    }

    /**
     * 删除应用
     *
     * @param appId
     * @return
     */
    @Caching(evict = {
            @CacheEvict(value = {"apps"}, key = "#appId"),
            @CacheEvict(value = {"apps"}, key = "'client:'+#appId")
    })
    @Override
    public void removeApp(Long appId) {
        SysApp sysAppInfo = getAppInfo(appId);
        if (sysAppInfo == null) {
            throw new PhenixAlertException(appId + "应用不存在!");
        }
        if (BaseConstants.ENABLED.equals(sysAppInfo.getIsPersist())) {
            throw new PhenixAlertException(String.format("保留数据,不允许删除"));
        }
        // 移除应用权限
        authorityService.removeAuthorityApp(appId);
        this.removeById(sysAppInfo.getAppId());
        phenixJdbcClientDetailsService.removeClientDetails(sysAppInfo.getApiKey());
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String apiKey = String.valueOf(RandomUtils.randomAlpha(24));
        String secretKey = String.valueOf(RandomUtils.randomAlpha(32));
        System.out.println("apiKey=" + apiKey);
        System.out.println("secretKey=" + secretKey);
        System.out.println("encodeSecretKey=" + encoder.encode(secretKey));
    }

}
