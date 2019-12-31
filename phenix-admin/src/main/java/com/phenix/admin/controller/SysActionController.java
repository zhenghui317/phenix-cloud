package com.phenix.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phenix.admin.entity.SysAction;
import com.phenix.admin.service.ISysActionService;
import com.phenix.core.security.http.PhenixRestTemplate;
import com.phenix.defines.response.ResponseMessage;
import io.swagger.annotations.Api;
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
 * @date 2019-12-16
 */
@Slf4j
@SuppressWarnings("unchecked")
@Api(tags = "系统功能按钮管理")
@RestController
@RequestMapping("/action")
public class SysActionController {
    @Autowired
    private ISysActionService actionService;
    @Autowired
    private PhenixRestTemplate dingJustRestTemplate;


    @ApiOperation(value = "获取数据", notes = "获取单个数据")
    @ApiImplicitParam(value = "主键", name = "actionId", required = true)
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<SysAction> get(@RequestParam Long actionId) {
        SysAction entity = actionService.getById(actionId);
        if (entity != null) {
            return ResponseMessage.ok(entity);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "删除数据", notes = "删除单个数据")
    @ApiImplicitParam(value = "主键", name = "actionId", required = true)
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage delete(@RequestParam Long actionId) {
        Boolean ok = actionService.removeById(actionId);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "新增数据", notes = "新增数据")
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage save(@RequestBody SysAction SysAction) {
        Boolean success = actionService.addAction(SysAction);
        if (success) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "更新数据", notes = "根据主键更新数据")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage update(@RequestBody SysAction SysAction) {
        Boolean ok = actionService.updateById(SysAction);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "保存或更新数据", notes = "保存或更新数据")
    @PostMapping(value = "/saveOrUpdate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveOrUpdate(@RequestBody SysAction SysAction) {
        Boolean ok = actionService.saveOrUpdate(SysAction);
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
    public ResponseMessage<IPage<SysAction>> page(@RequestParam Integer page, @RequestParam Integer size, SysAction entity) {
        QueryWrapper<SysAction> wrapper = new QueryWrapper<>(entity);
        IPage<SysAction> respIPage = actionService.page(new Page(page, size), wrapper);
        if (respIPage != null) {
            return ResponseMessage.ok(respIPage);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "查询数据集合", notes = "查询数据集合")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<List<SysAction>> list(SysAction entity) {
        QueryWrapper<SysAction> wrapper = new QueryWrapper<>(entity);
        List<SysAction> list = actionService.list(wrapper);
        if (list != null) {
            return ResponseMessage.ok(list);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量新增数据", notes = "批量新增数据")
    @PostMapping(value = "/saveBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveBatch(@RequestBody List<SysAction> sysActions) {
        Boolean ok = actionService.saveBatch(sysActions);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量更新数据", notes = "批量更新数据")
    @PutMapping(value = "/updateBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage updateBatch(@RequestBody List<SysAction> sysActions) {
        Boolean ok = actionService.saveOrUpdateBatch(sysActions);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量删除数据", notes = "批量删除数据")
    @PostMapping(value = "/deleteBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage deleteBatch(@RequestBody List<Long> actionIds) {
        Boolean ok = actionService.removeByIds(actionIds);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }
}
