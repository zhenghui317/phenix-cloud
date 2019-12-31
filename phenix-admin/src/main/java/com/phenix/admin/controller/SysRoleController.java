package com.phenix.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phenix.admin.entity.SysRole;
import com.phenix.admin.entity.SysRoleUser;
import com.phenix.admin.pojo.parameter.RoleUsersParameter;
import com.phenix.admin.service.ISysRoleService;
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
@Api(tags = "系统角色管理")
@RestController
@RequestMapping("/role")
public class SysRoleController {
    @Autowired
    private ISysRoleService roleService;

    @ApiOperation(value = "获取数据", notes = "获取单个数据")
    @ApiImplicitParam(value = "主键", name = "roleId", required = true)
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<SysRole> get(@RequestParam Long roleId) {
        SysRole entity = roleService.getById(roleId);
        if(entity != null){
            return ResponseMessage.ok(entity);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "删除数据", notes = "删除单个数据")
    @ApiImplicitParam(value = "主键", name = "roleId", required = true)
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage delete(@RequestParam Long roleId){
        Boolean ok = roleService.removeById(roleId);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "新增数据", notes = "新增数据")
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage save(@RequestBody SysRole sysRole) {
        Boolean ok = roleService.save(sysRole);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "更新数据", notes = "根据主键更新数据")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage update(@RequestBody SysRole sysRole){
        Boolean ok = roleService.updateById(sysRole);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "保存或更新数据", notes = "保存或更新数据")
    @PostMapping(value = "/saveOrUpdate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveOrUpdate(@RequestBody SysRole sysRole){
        Boolean ok = roleService.saveOrUpdate(sysRole);
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
    public ResponseMessage<IPage<SysRole>> page(@RequestParam Integer page, @RequestParam Integer size, SysRole entity){
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>(entity);
        IPage<SysRole> respIPage = roleService.page(new Page(page,size), wrapper);
        if(respIPage != null){
            return ResponseMessage.ok(respIPage);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "查询数据集合", notes = "查询数据集合")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<List<SysRole>> list(SysRole entity){
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>(entity);
        List<SysRole> list = roleService.list(wrapper);
        if(list != null){
            return ResponseMessage.ok(list);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量新增数据", notes = "批量新增数据")
    @PostMapping(value = "/saveBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveBatch(@RequestBody List<SysRole> sysRoles) {
        Boolean ok = roleService.saveBatch(sysRoles);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量更新数据", notes = "批量更新数据")
    @PutMapping(value = "/updateBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage updateBatch(@RequestBody List<SysRole> sysRoles) {
        Boolean ok = roleService.saveOrUpdateBatch(sysRoles);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量删除数据", notes = "批量删除数据")
    @PostMapping(value = "/deleteBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage deleteBatch(@RequestBody List<Long> roleIds) {
        Boolean ok = roleService.removeByIds(roleIds);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    /**
     * 获取所有角色列表
     *
     * @return
     */
    @ApiOperation(value = "获取所有角色列表", notes = "获取所有角色列表")
    @GetMapping("/all")
    public ResponseMessage<List<SysRole>> getRoleAllList() {
        return ResponseMessage.ok(roleService.findAllList());
    }


    /**
     * 角色添加成员
     * @param roleUsersParameter
     * @return
     */
    @ApiOperation(value = "角色添加成员", notes = "角色添加成员")
    @PostMapping("/role/users/add")
    public ResponseMessage addUserRoles(@RequestBody RoleUsersParameter roleUsersParameter
                                        ) {
        roleService.saveRoleUsers(roleUsersParameter.getRoleId(), roleUsersParameter.getUserIds());
        return ResponseMessage.ok();
    }

    /**
     * 查询角色成员
     *
     * @param roleId
     * @return
     */
    @ApiOperation(value = "查询角色成员", notes = "查询角色成员")
    @GetMapping("/users")
    public ResponseMessage<List<SysRoleUser>> getRoleUsers(
            @RequestParam(value = "roleId") Long roleId
    ) {
        return ResponseMessage.ok(roleService.findRoleUsers(roleId));
    }

}
