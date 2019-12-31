package com.phenix.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phenix.admin.entity.SysDictType;
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
import com.phenix.admin.service.ISysDictDataService;
import com.phenix.admin.entity.SysDictData;

import java.util.List;
/**
 * <p>
 * 字典数据表 前端控制器
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-25
 */
@Slf4j
@SuppressWarnings("unchecked")
@Api(tags = "字典数据表")
@RestController
@RequestMapping("/dictData")
public class SysDictDataController {

    @Autowired
    private ISysDictDataService sysDictDataService;

    @ApiOperation(value = "获取数据", notes = "获取单个数据")
    @ApiImplicitParam(value = "主键", name = "dictCode", required = true)
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<SysDictData> get(@RequestParam Long dictCode) {
        SysDictData entity = sysDictDataService.getById(dictCode);
        if(entity != null){
            return ResponseMessage.ok(entity);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "删除数据", notes = "删除单个数据")
    @ApiImplicitParam(value = "主键", name = "dictCode", required = true)
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage delete(@RequestParam Long dictCode){
        Boolean ok = sysDictDataService.removeById(dictCode);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "新增数据", notes = "新增数据")
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage save(@RequestBody SysDictData sysDictData) {
        Boolean ok = sysDictDataService.save(sysDictData);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "更新数据", notes = "根据主键更新数据")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage update(@RequestBody SysDictData sysDictData){
        Boolean ok = sysDictDataService.updateById(sysDictData);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "保存或更新数据", notes = "保存或更新数据")
    @PostMapping(value = "/saveOrUpdate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveOrUpdate(@RequestBody SysDictData sysDictData){
        Boolean ok = sysDictDataService.saveOrUpdate(sysDictData);
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
    public ResponseMessage<IPage<SysDictData>> page(@RequestParam Integer page,@RequestParam Integer size, SysDictData entity){
        QueryWrapper<SysDictData> wrapper = new QueryWrapper<>(entity);
        IPage<SysDictData> respIPage = sysDictDataService.page(new Page(page,size), wrapper);
        if(respIPage != null){
            return ResponseMessage.ok(respIPage);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "查询数据集合", notes = "查询数据集合")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<List<SysDictData>> list(SysDictData entity){
        QueryWrapper<SysDictData> wrapper = new QueryWrapper<>(entity);
        List<SysDictData> list = sysDictDataService.list(wrapper);
        if(list != null){
            return ResponseMessage.ok(list);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量新增数据", notes = "批量新增数据")
    @PostMapping(value = "/saveBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveBatch(@RequestBody List<SysDictData> sysDictDatas) {
        Boolean ok = sysDictDataService.saveBatch(sysDictDatas);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量更新数据", notes = "批量更新数据")
    @PutMapping(value = "/updateBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage updateBatch(@RequestBody List<SysDictData> sysDictDatas) {
        Boolean ok = sysDictDataService.saveOrUpdateBatch(sysDictDatas);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量删除数据", notes = "批量删除数据")
    @PostMapping(value = "/deleteBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage deleteBatch(@RequestBody List<Long> dictCodes) {
        Boolean ok = sysDictDataService.removeByIds(dictCodes);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    /**
     * 根据DictCode查询
     * @param dictType
     * @return
     */
    @ApiOperation(value = "根据字典类型查询数据集合", notes = "根据字典类型查询数据集合")
    @GetMapping(value = "/getDictDatas", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<List<SysDictData>> getDictTypes(String dictType){
        List<SysDictData> list = sysDictDataService.getDictDatasByDictType(dictType);
        if(list != null){
            return ResponseMessage.ok(list);
        }
        return ResponseMessage.fail();
    }
}
