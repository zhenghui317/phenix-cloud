package com.phenix.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phenix.admin.entity.OauthClientDetails;
import com.phenix.admin.service.IOauthClientDetailsService;
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
 * <p>
 * oauth2客户端信息 前端控制器
 * </p>
 *
 * @author zhenghui
 * @since 2019-12-13
 */
@Slf4j
@SuppressWarnings("unchecked")
@Api(description = "oauth2客户端信息")
@RestController
@RequestMapping("/oauthClientDetails")
public class OauthClientDetailsController {

    @Autowired
    private IOauthClientDetailsService oauthClientDetailsService;

    @ApiOperation(value = "获取数据", notes = "获取单个数据")
    @ApiImplicitParam(value = "主键", name = "id", required = true)
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<OauthClientDetails> get(@RequestParam Long id) {
        OauthClientDetails entity = oauthClientDetailsService.getById(id);
        if(entity != null){
            return ResponseMessage.ok(entity);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "删除数据", notes = "删除单个数据")
    @ApiImplicitParam(value = "主键", name = "id", required = true)
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage delete(@RequestParam Long id){
        Boolean ok = oauthClientDetailsService.removeById(id);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "新增数据", notes = "新增数据")
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage save(@RequestBody OauthClientDetails oauthClientDetails) {
        Boolean ok = oauthClientDetailsService.save(oauthClientDetails);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "更新数据", notes = "根据主键更新数据")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage updateById(@RequestBody OauthClientDetails oauthClientDetails){
        Boolean ok = oauthClientDetailsService.updateById(oauthClientDetails);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "保存或更新数据", notes = "保存或更新数据")
    @PostMapping(value = "/saveOrUpdate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveOrUpdate(@RequestBody OauthClientDetails oauthClientDetails){
        Boolean ok = oauthClientDetailsService.saveOrUpdate(oauthClientDetails);
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
    public ResponseMessage<IPage<OauthClientDetails>> page(@RequestParam Integer page,@RequestParam Integer size, OauthClientDetails entity){
        QueryWrapper<OauthClientDetails> wrapper = new QueryWrapper<>(entity);
        IPage<OauthClientDetails> respIPage = oauthClientDetailsService.page(new Page(page,size), wrapper);
        if(respIPage != null){
            return ResponseMessage.ok(respIPage);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "查询数据集合", notes = "查询数据集合")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<IPage<OauthClientDetails>> list(OauthClientDetails entity){
        QueryWrapper<OauthClientDetails> wrapper = new QueryWrapper<>(entity);
        List<OauthClientDetails> list = oauthClientDetailsService.list(wrapper);
        if(list != null){
            return ResponseMessage.ok(list);
        }
        return ResponseMessage.fail();
    }

}
