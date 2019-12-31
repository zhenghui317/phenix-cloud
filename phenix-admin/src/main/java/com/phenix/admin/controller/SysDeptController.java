package com.phenix.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phenix.admin.entity.SysRole;
import com.phenix.admin.pojo.vo.ZTreeVO;
import com.phenix.defines.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiImplicitParam;

import lombok.extern.slf4j.Slf4j;
import com.phenix.admin.service.ISysDeptService;
import com.phenix.admin.entity.SysDept;

import java.util.List;
/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-25
 */
@Slf4j
@SuppressWarnings("unchecked")
@Api(tags = "部门表")
@RestController
@RequestMapping("/dept")
public class SysDeptController {

    @Autowired
    private ISysDeptService sysDeptService;

    @ApiOperation(value = "获取数据", notes = "获取单个数据")
    @ApiImplicitParam(value = "主键", name = "deptId", required = true)
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<SysDept> get(@RequestParam Long deptId) {
        SysDept entity = sysDeptService.getById(deptId);
        if(entity != null){
            return ResponseMessage.ok(entity);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "删除数据", notes = "删除单个数据")
    @ApiImplicitParam(value = "主键", name = "deptId", required = true)
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage delete(@RequestParam Long deptId){
        Boolean ok = sysDeptService.removeById(deptId);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "新增数据", notes = "新增数据")
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage save(@RequestBody SysDept sysDept) {
        Boolean ok = sysDeptService.save(sysDept);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "更新数据", notes = "根据主键更新数据")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage update(@RequestBody SysDept sysDept){
        Boolean ok = sysDeptService.updateById(sysDept);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "保存或更新数据", notes = "保存或更新数据")
    @PostMapping(value = "/saveOrUpdate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveOrUpdate(@RequestBody SysDept sysDept){
        Boolean ok = sysDeptService.saveOrUpdate(sysDept);
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
    public ResponseMessage<IPage<SysDept>> page(@RequestParam Integer page,@RequestParam Integer size, SysDept entity){
        QueryWrapper<SysDept> wrapper = new QueryWrapper<>(entity);
        IPage<SysDept> respIPage = sysDeptService.page(new Page(page,size), wrapper);
        if(respIPage != null){
            return ResponseMessage.ok(respIPage);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "查询数据集合", notes = "查询数据集合")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<List<SysDept>> list(SysDept entity){
        QueryWrapper<SysDept> wrapper = new QueryWrapper<>(entity);
        List<SysDept> list = sysDeptService.list(wrapper);
        if(list != null){
            return ResponseMessage.ok(list);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量新增数据", notes = "批量新增数据")
    @PostMapping(value = "/saveBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveBatch(@RequestBody List<SysDept> sysDepts) {
        Boolean ok = sysDeptService.saveBatch(sysDepts);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量更新数据", notes = "批量更新数据")
    @PutMapping(value = "/updateBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage updateBatch(@RequestBody List<SysDept> sysDepts) {
        Boolean ok = sysDeptService.saveOrUpdateBatch(sysDepts);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量删除数据", notes = "批量删除数据")
    @PostMapping(value = "/deleteBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage deleteBatch(@RequestBody List<Long> deptIds) {
        Boolean ok = sysDeptService.removeByIds(deptIds);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }


    /**
     * 加载部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<ZTreeVO> treeData()
    {
        List<ZTreeVO> ztrees = sysDeptService.selectDeptTree(new SysDept());
        return ztrees;
    }

    /**
     * 加载角色部门（数据权限）列表树
     */
    @GetMapping("/roleDeptTreeData")
    @ResponseBody
    public List<ZTreeVO> deptTreeData(SysRole role)
    {
        List<ZTreeVO> ztrees = sysDeptService.roleDeptTreeData(role);
        return ztrees;
    }
}
