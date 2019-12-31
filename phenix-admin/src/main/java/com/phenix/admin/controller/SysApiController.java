package com.phenix.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phenix.admin.entity.SysApi;
import com.phenix.admin.service.ISysApiService;
import com.phenix.core.security.http.PhenixRestTemplate;
import com.phenix.defines.response.ResponseMessage;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhenghui
 */
@io.swagger.annotations.Api(tags = "系统接口资源管理")
@RestController
@Slf4j
@SuppressWarnings("unchecked")
@RequestMapping("/api")
public class SysApiController {
    @Autowired
    private ISysApiService apiService;
    @Autowired
    private PhenixRestTemplate dingJustRestTemplate;

    @ApiOperation(value = "获取数据", notes = "获取单个数据")
    @ApiImplicitParam(value = "主键", name = "id", required = true)
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<SysApi> get(@RequestParam Long id) {
        SysApi entity = apiService.getById(id);
        if(entity != null){
            return ResponseMessage.ok(entity);
        }
        return ResponseMessage.fail();
    }

    /**
     * 移除接口资源
     *
     * @param apiId
     * @return
     */
    @ApiOperation(value = "移除接口资源", notes = "移除接口资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "apiId", required = true, value = "ApiId", paramType = "form"),
    })
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage delete(@RequestParam("apiId") Long apiId) {
        Boolean ok = apiService.removeApi(apiId);
        // 刷新网关
        dingJustRestTemplate.refreshGateway();
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    /**
     * 添加接口资源
     *
     * @param sysApi   api对象
     * @return
     */
    @ApiOperation(value = "添加接口资源", notes = "添加接口资源")
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage save(@RequestBody SysApi sysApi) {
        Boolean ok = apiService.addApi(sysApi);
        dingJustRestTemplate.refreshGateway();
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    /**
     * 编辑接口资源
     *
     * @param sysApi     api对象
     * @return
     */
    @ApiOperation(value = "编辑接口资源", notes = "编辑接口资源")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage update(@RequestBody SysApi sysApi) {
        Boolean ok = apiService.updateApi(sysApi);
        // 刷新网关
        dingJustRestTemplate.refreshGateway();
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "保存或更新数据", notes = "保存或更新数据")
    @PostMapping(value = "/saveOrUpdate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveOrUpdate(@RequestBody SysApi sysApi){
        Boolean ok = apiService.saveOrUpdate(sysApi);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "查询分页数据", notes = "查询分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "分页数，从1开始", name = "page", required = true),
            @ApiImplicitParam(value = "每页大小", name = "size", required = true)})
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<IPage<SysApi>> page(@RequestParam Integer page, @RequestParam Integer size, SysApi entity){
        QueryWrapper<SysApi> wrapper = new QueryWrapper<SysApi>(entity);
        IPage<SysApi> respIPage = apiService.page(new Page(page,size), wrapper);
        if(respIPage != null){
            return ResponseMessage.ok(respIPage);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "查询数据集合", notes = "查询数据集合")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<IPage<SysApi>> list(SysApi entity){
        QueryWrapper<SysApi> wrapper = new QueryWrapper<SysApi>(entity);
        List<SysApi> list = apiService.list(wrapper);
        if(list != null){
            return ResponseMessage.ok(list);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量新增数据", notes = "批量新增数据")
    @PostMapping(value = "/saveBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveBatch(@RequestBody List<SysApi> sysApis) {
        Boolean ok = apiService.saveBatch(sysApis);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量更新数据", notes = "批量更新数据")
    @PutMapping(value = "/updateBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage updateBatch(@RequestBody List<SysApi> sysApis) {
        Boolean ok = apiService.saveOrUpdateBatch(sysApis);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量删除数据", notes = "批量删除数据")
    @DeleteMapping(value = "/deleteBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage deleteBatch(@RequestBody List<Long> apiIds) {
        Boolean ok = apiService.removeByIds(apiIds);
        // 刷新网关
        dingJustRestTemplate.refreshGateway();
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }
}
