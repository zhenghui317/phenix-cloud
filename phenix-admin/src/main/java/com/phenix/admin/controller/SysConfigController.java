package com.phenix.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phenix.defines.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import com.phenix.admin.service.ISysConfigService;
import com.phenix.admin.entity.SysConfig;

import java.util.List;
/**
 * <p>
 * 参数配置表 前端控制器
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-25
 */
@Slf4j
@SuppressWarnings("unchecked")
@Api(tags = "参数配置表")
@RestController
@RequestMapping("/config")
public class SysConfigController {

    @Autowired
    private ISysConfigService sysConfigService;

    @ApiOperation(value = "获取数据", notes = "获取单个数据")
    @ApiImplicitParam(value = "主键", name = "configId", required = true)
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<SysConfig> get(@RequestParam Integer configId) {
        SysConfig entity = sysConfigService.getById(configId);
        if(entity != null){
            return ResponseMessage.ok(entity);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "删除数据", notes = "删除单个数据")
    @ApiImplicitParam(value = "主键", name = "configId", required = true)
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage delete(@RequestParam Integer configId){
        Boolean ok = sysConfigService.removeById(configId);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "新增数据", notes = "新增数据")
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage save(@RequestBody SysConfig sysConfig) {
        Boolean ok = sysConfigService.save(sysConfig);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "更新数据", notes = "根据主键更新数据")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage update(@RequestBody SysConfig sysConfig){
        Boolean ok = sysConfigService.updateById(sysConfig);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "保存或更新数据", notes = "保存或更新数据")
    @PostMapping(value = "/saveOrUpdate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveOrUpdate(@RequestBody SysConfig sysConfig){
        Boolean ok = sysConfigService.saveOrUpdate(sysConfig);
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
    public ResponseMessage<IPage<SysConfig>> page(@RequestParam Integer page,@RequestParam Integer size, SysConfig entity){
        QueryWrapper<SysConfig> wrapper = new QueryWrapper<>(entity);
        IPage<SysConfig> respIPage = sysConfigService.page(new Page(page,size), wrapper);
        if(respIPage != null){
            return ResponseMessage.ok(respIPage);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "查询数据集合", notes = "查询数据集合")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<List<SysConfig>> list(SysConfig entity){
        QueryWrapper<SysConfig> wrapper = new QueryWrapper<>(entity);
        List<SysConfig> list = sysConfigService.list(wrapper);
        if(list != null){
            return ResponseMessage.ok(list);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量新增数据", notes = "批量新增数据")
    @PostMapping(value = "/saveBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveBatch(@RequestBody List<SysConfig> sysConfigs) {
        Boolean ok = sysConfigService.saveBatch(sysConfigs);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量更新数据", notes = "批量更新数据")
    @PutMapping(value = "/updateBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage updateBatch(@RequestBody List<SysConfig> sysConfigs) {
        Boolean ok = sysConfigService.saveOrUpdateBatch(sysConfigs);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量删除数据", notes = "批量删除数据")
    @PostMapping(value = "/deleteBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage deleteBatch(@RequestBody List<Integer> configIds) {
        Boolean ok = sysConfigService.removeByIds(configIds);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }


    /**
     * 根据DictCode查询
     * @param configKey
     * @return
     */
    @ApiOperation(value = "根据配置名称查询配置", notes = "根据配置名称查询配置")
    @GetMapping(value = "/getSysConfig", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<List<SysConfig>> getSysConfigByConfigKey(String configKey){
        SysConfig entity = sysConfigService.getSysConfigByConfigKey(configKey);
        if(entity != null){
            return ResponseMessage.ok(entity);
        }
        return ResponseMessage.fail();
    }
}
