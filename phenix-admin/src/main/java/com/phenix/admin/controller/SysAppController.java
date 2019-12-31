package com.phenix.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phenix.admin.entity.SysApp;
import com.phenix.admin.service.ISysAppService;
import com.phenix.core.security.PhenixClientDetails;
import com.phenix.core.security.http.PhenixRestTemplate;
import com.phenix.core.utils.BeanConvertUtils;
import com.phenix.defines.response.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 系统用户信息
 *
 * @author zhenghui
 */
@Api(tags = "系统应用管理")
@RestController
@Slf4j
@SuppressWarnings("unchecked")
@RequestMapping("/app")
public class SysAppController {
    @Autowired
    private ISysAppService appService;
    @Autowired
    private PhenixRestTemplate dingJustRestTemplate;

    @ApiOperation(value = "获取数据", notes = "获取单个数据")
    @ApiImplicitParam(value = "主键", name = "appId", required = true)
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<SysApp> get(@RequestParam Long appId) {
        SysApp entity = appService.getById(appId);
        if (entity != null) {
            return ResponseMessage.ok(entity);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "删除数据", notes = "删除单个数据")
    @ApiImplicitParam(value = "主键", name = "appId", required = true)
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage delete(@RequestParam Long appId) {
        Boolean ok = appService.removeById(appId);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "新增数据", notes = "新增数据")
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage save(@RequestBody SysApp sysApp) {
        Boolean ok = appService.save(sysApp);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "更新数据", notes = "根据主键更新数据")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage update(@RequestBody SysApp sysApp) {
        Boolean ok = appService.updateById(sysApp);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "保存或更新数据", notes = "保存或更新数据")
    @PostMapping(value = "/saveOrUpdate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveOrUpdate(@RequestBody SysApp sysApp) {
        Boolean ok = appService.saveOrUpdate(sysApp);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "查询分页数据", notes = "查询分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "分页数，从1开始", name = "page", required = true),
            @ApiImplicitParam(value = "每页大小", name = "size", required = true)})
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<IPage<SysApp>> page(@RequestParam Integer page, @RequestParam Integer size, SysApp entity) {
        QueryWrapper<SysApp> wrapper = new QueryWrapper<>(entity);
        IPage<SysApp> respIPage = appService.page(new Page(page, size), wrapper);
        if (respIPage != null) {
            return ResponseMessage.ok(respIPage);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "查询数据集合", notes = "查询数据集合")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<List<SysApp>> list(SysApp entity) {
        QueryWrapper<SysApp> wrapper = new QueryWrapper<>(entity);
        List<SysApp> list = appService.list(wrapper);
        if (list != null) {
            return ResponseMessage.ok(list);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量新增数据", notes = "批量新增数据")
    @PostMapping(value = "/saveBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveBatch(@RequestBody List<SysApp> sysApps) {
        Boolean ok = appService.saveBatch(sysApps);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量更新数据", notes = "批量更新数据")
    @PutMapping(value = "/updateBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage updateBatch(@RequestBody List<SysApp> sysApps) {
        Boolean ok = appService.saveOrUpdateBatch(sysApps);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量删除数据", notes = "批量删除数据")
    @PostMapping(value = "/deleteBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage deleteBatch(@RequestBody List<String> appIds) {
        Boolean ok = appService.removeByIds(appIds);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    /**
     * 获取应用开发配置信息
     *
     * @param clientId
     * @return
     */
    @ApiOperation(value = "获取应用开发配置信息", notes = "获取应用开发配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "应用ID", defaultValue = "1", required = true, paramType = "path"),
    })
    @GetMapping(value = "/client/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<PhenixClientDetails> getAppClientInfo(
            @RequestParam("clientId") String clientId
    ) {
        PhenixClientDetails clientInfo = appService.getAppClientInfo(clientId);
        if (clientInfo != null) {
            return ResponseMessage.ok(clientInfo);
        }
        return ResponseMessage.fail();
    }


    /**
     * 完善应用开发信息
     *
     * @param appId                应用名称
     * @param grantTypes           授权类型(多个使用,号隔开)
     * @param redirectUrls         第三方应用授权回调地址(多个使用,号隔开)
     * @param scopes               用户授权范围(多个使用,号隔开)
     * @param autoApproveScopes    用户自动授权范围(多个使用,号隔开)
     * @param accessTokenValidity  令牌有效期(秒)
     * @param refreshTokenValidity 刷新令牌有效期(秒)
     * @return
     */
    @ApiOperation(value = "完善应用开发信息", notes = "完善应用开发信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用Id", required = true, paramType = "form"),
            @ApiImplicitParam(name = "grantTypes", value = "授权类型(多个使用,号隔开)", required = true, paramType = "form"),
            @ApiImplicitParam(name = "redirectUrls", value = "第三方应用授权回调地址", required = true, paramType = "form"),
            @ApiImplicitParam(name = "scopes", value = "用户授权范围(多个使用,号隔开)", required = true, paramType = "form"),
            @ApiImplicitParam(name = "autoApproveScopes", value = "用户自动授权范围(多个使用,号隔开)", required = false, paramType = "form"),
            @ApiImplicitParam(name = "accessTokenValidity", value = "令牌有效期(秒)", required = true, paramType = "form"),
            @ApiImplicitParam(name = "refreshTokenValidity", value = "刷新令牌有效期(秒)", required = true, paramType = "form")
    })
    @PostMapping(value = "/client/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage updateAppClientInfo(
            @RequestParam("appId") Long appId,
            @RequestParam(value = "grantTypes") String grantTypes,
            @RequestParam(value = "redirectUrls") String redirectUrls,
            @RequestParam(value = "scopes") String scopes,
            @RequestParam(value = "accessTokenValidity", required = true) Integer accessTokenValidity,
            @RequestParam(value = "refreshTokenValidity", required = true) Integer refreshTokenValidity,
            @RequestParam(value = "autoApproveScopes", required = false) String autoApproveScopes
    ) {
        SysApp sysApp = appService.getAppInfo(appId);
        PhenixClientDetails client = new PhenixClientDetails(sysApp.getApiKey(), "", scopes, grantTypes, "", redirectUrls);
        client.setAccessTokenValiditySeconds(accessTokenValidity);
        client.setRefreshTokenValiditySeconds(refreshTokenValidity);
        client.setAutoApproveScopes(autoApproveScopes != null ? Arrays.asList(autoApproveScopes.split(",")) : null);
        Map info = BeanConvertUtils.objectToMap(sysApp);
        client.setAdditionalInformation(info);
        appService.updateAppClientInfo(client);
        return ResponseMessage.ok();
    }


    /**
     * 重置应用秘钥
     *
     * @param appId 应用Id
     * @return
     */
    @ApiOperation(value = "重置应用秘钥", notes = "重置应用秘钥")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用Id", required = true, paramType = "form"),
    })
    @PostMapping(value = "/reset", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<String> resetAppSecret(
            @RequestParam("appId") Long appId
    ) {
        String result = appService.restSecret(appId);
        return ResponseMessage.ok(result);
    }

}
