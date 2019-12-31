package com.phenix.admin.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phenix.admin.entity.SysAction;
import com.phenix.admin.entity.SysMenu;
import com.phenix.admin.service.ISysActionService;
import com.phenix.admin.service.ISysMenuService;
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
 */
@Slf4j
@SuppressWarnings("unchecked")
@Api(tags = "系统菜单资源管理")
@RestController
@RequestMapping("/menu")
public class SysMenuController {
    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysActionService actionService;

    @Autowired
    private PhenixRestTemplate dingJustRestTemplate;


    @ApiOperation(value = "获取数据", notes = "获取单个数据")
    @ApiImplicitParam(value = "主键", name = "menuId", required = true)
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<SysMenu> get(@RequestParam Long menuId) {
        SysMenu entity = menuService.getById(menuId);
        if (entity != null) {
            return ResponseMessage.ok(entity);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "删除数据", notes = "删除单个数据")
    @ApiImplicitParam(value = "主键", name = "menuId", required = true)
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage delete(@RequestParam Long menuId) {
        Boolean ok = menuService.removeMenu(menuId);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "新增数据", notes = "新增数据")
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage save(@RequestBody SysMenu sysMenu) {
        Boolean ok = menuService.addMenu(sysMenu);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "更新数据", notes = "根据主键更新数据")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage update(@RequestBody SysMenu sysMenu) {
        Boolean ok = menuService.updateMenu(sysMenu);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "保存或更新数据", notes = "保存或更新数据")
    @PostMapping(value = "/saveOrUpdate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveOrUpdate(@RequestBody SysMenu sysMenu) {
        Boolean ok = menuService.saveOrUpdate(sysMenu);
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
    public ResponseMessage<IPage<SysMenu>> page(@RequestParam Integer page, @RequestParam Integer size, SysMenu entity) {
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>(entity);
        IPage<SysMenu> respIPage = menuService.page(new Page(page, size), wrapper);
        if (respIPage != null) {
            return ResponseMessage.ok(respIPage);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "查询数据集合", notes = "查询数据集合")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<List<SysMenu>> list(SysMenu entity) {
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>(entity);
        List<SysMenu> list = menuService.list(wrapper);
        if (list != null) {
            return ResponseMessage.ok(list);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量新增数据", notes = "批量新增数据")
    @PostMapping(value = "/saveBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveBatch(@RequestBody List<SysMenu> sysMenus) {
        Boolean ok = menuService.saveBatch(sysMenus);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量更新数据", notes = "批量更新数据")
    @PutMapping(value = "/updateBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage updateBatch(@RequestBody List<SysMenu> sysMenus) {
        Boolean ok = menuService.saveOrUpdateBatch(sysMenus);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量删除数据", notes = "批量删除数据")
    @PostMapping(value = "/deleteBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage deleteBatch(@RequestBody List<Long> menuIds) {
        Boolean ok = menuService.removeByIds(menuIds);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }


    /**
     * 获取菜单下所有操作
     *
     * @param menuId
     * @return
     */
    @ApiOperation(value = "获取菜单下所有操作", notes = "获取菜单下所有操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "menuId", paramType = "form"),
    })
    @GetMapping("/actions")
    public ResponseMessage<List<SysAction>> getMenuAction(Long menuId) {
        List<SysAction> sysActions = actionService.findListByMenuId(menuId);
        if (CollectionUtil.isNotEmpty(sysActions)) {
            return ResponseMessage.ok(sysActions);
        }
        return ResponseMessage.fail();
    }
}
