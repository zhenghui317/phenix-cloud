package com.phenix.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phenix.admin.entity.SysAuthority;
import com.phenix.admin.entity.SysAuthorityAction;
import com.phenix.admin.entity.SysUser;
import com.phenix.admin.pojo.dto.AuthoritySysApiDTO;
import com.phenix.admin.pojo.dto.AuthoritySysMenuDTO;
import com.phenix.admin.pojo.dto.AuthorityResourceDTO;
import com.phenix.admin.pojo.parameter.AuthorityActionParameter;
import com.phenix.admin.pojo.parameter.AuthorityAppParameter;
import com.phenix.admin.pojo.parameter.AuthorityRoleParameter;
import com.phenix.admin.pojo.parameter.AuthorityUserParameter;
import com.phenix.admin.service.ISysAuthorityService;
import com.phenix.admin.service.ISysUserService;
import com.phenix.core.security.authority.PhenixAuthority;
import com.phenix.core.security.http.PhenixRestTemplate;
import com.phenix.defines.constants.CommonConstants;
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
 * @author: zhenghui
 * @date: 2018/11/26 18:20
 * @description:
 */
@Slf4j
@SuppressWarnings("unchecked")
@Api(tags = "系统权限管理")
@RestController
@RequestMapping("/authority")
public class SysAuthorityController {

    @Autowired
    private ISysAuthorityService authorityService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private PhenixRestTemplate dingJustRestTemplate;


    @ApiOperation(value = "获取数据", notes = "获取单个数据")
    @ApiImplicitParam(value = "主键", name = "authorityId", required = true)
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<SysAuthority> get(@RequestParam Long authorityId) {
        SysAuthority entity = authorityService.getById(authorityId);
        if (entity != null) {
            return ResponseMessage.ok(entity);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "删除数据", notes = "删除单个数据")
    @ApiImplicitParam(value = "主键", name = "authorityId", required = true)
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage delete(@RequestParam Long authorityId) {
        Boolean ok = authorityService.removeById(authorityId);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "新增数据", notes = "新增数据")
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage save(@RequestBody SysAuthority sysAuthority) {
        Boolean ok = authorityService.save(sysAuthority);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "更新数据", notes = "根据主键更新数据")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage update(@RequestBody SysAuthority sysAuthority) {
        Boolean ok = authorityService.updateById(sysAuthority);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "保存或更新数据", notes = "保存或更新数据")
    @PostMapping(value = "/saveOrUpdate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveOrUpdate(@RequestBody SysAuthority sysAuthority) {
        Boolean ok = authorityService.saveOrUpdate(sysAuthority);
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
    public ResponseMessage<IPage<SysAuthority>> page(@RequestParam Integer page, @RequestParam Integer size, SysAuthority entity) {
        QueryWrapper<SysAuthority> wrapper = new QueryWrapper<>(entity);
        IPage<SysAuthority> respIPage = authorityService.page(new Page(page, size), wrapper);
        if (respIPage != null) {
            return ResponseMessage.ok(respIPage);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "查询数据集合", notes = "查询数据集合")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<List<SysAuthority>> list(SysAuthority entity) {
        QueryWrapper<SysAuthority> wrapper = new QueryWrapper<>(entity);
        List<SysAuthority> list = authorityService.list(wrapper);
        if (list != null) {
            return ResponseMessage.ok(list);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量新增数据", notes = "批量新增数据")
    @PostMapping(value = "/saveBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveBatch(@RequestBody List<SysAuthority> sysAuthorities) {
        Boolean ok = authorityService.saveBatch(sysAuthorities);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量更新数据", notes = "批量更新数据")
    @PutMapping(value = "/updateBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage updateBatch(@RequestBody List<SysAuthority> sysAuthorities) {
        Boolean ok = authorityService.saveOrUpdateBatch(sysAuthorities);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量删除数据", notes = "批量删除数据")
    @PostMapping(value = "/deleteBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage deleteBatch(@RequestBody List<Long> authorityIds) {
        Boolean ok = authorityService.removeByIds(authorityIds);
        if (ok) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    /**
     * 获取所有访问权限列表
     *
     * @return
     */
    @ApiOperation(value = "获取所有访问权限列表", notes = "获取所有访问权限列表")
    @GetMapping("/accesss")
    public ResponseMessage<List<AuthorityResourceDTO>> findAuthorityResource() {
        List<AuthorityResourceDTO> result = authorityService.findAuthorityResource();
        return ResponseMessage.ok(result);
    }

    /**
     * 获取权限列表
     *
     * @return
     */
    @ApiOperation(value = "获取接口权限列表", notes = "获取接口权限列表")
    @GetMapping("/apis")
    public ResponseMessage<List<AuthoritySysApiDTO>> findAuthorityApi(
            @RequestParam(value = "serviceId", required = false) String serviceId
    ) {
        List<AuthoritySysApiDTO> result = authorityService.findAuthorityApi(serviceId);
        return ResponseMessage.ok(result);
    }


    /**
     * 获取菜单权限列表
     *
     * @return
     */
    @ApiOperation(value = "获取菜单权限列表", notes = "获取菜单权限列表")
    @GetMapping("/menus")
    public ResponseMessage<List<AuthoritySysMenuDTO>> findAuthorityMenu() {
        List<AuthoritySysMenuDTO> result = authorityService.findAuthorityMenu(1);
        return ResponseMessage.ok(result);
    }

    /**
     * 获取功能权限列表
     *
     * @param actionId
     * @return
     */
    @ApiOperation(value = "获取功能权限列表", notes = "获取功能权限列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actionId", required = true, value = "功能按钮ID", paramType = "form")
    })
    @GetMapping("/actions")
    public ResponseMessage<List<SysAuthorityAction>> findAuthorityAction(
            @RequestParam(value = "actionId") Long actionId
    ) {
        List<SysAuthorityAction> list = authorityService.findAuthorityAction(actionId);
        return ResponseMessage.ok(list);
    }


    /**
     * 获取角色已分配权限
     *
     * @param roleId 角色ID
     * @return
     */
    @ApiOperation(value = "获取角色已分配权限", notes = "获取角色已分配权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "form")
    })
    @GetMapping("/roles")
    public ResponseMessage<List<PhenixAuthority>> findAuthorityRole(Long roleId) {
        List<PhenixAuthority> result = authorityService.findAuthorityByRole(roleId);
        return ResponseMessage.ok(result);
    }


    /**
     * 获取用户已分配权限
     *
     * @param userId 用户ID
     * @return
     */
    @ApiOperation(value = "获取用户已分配权限", notes = "获取用户已分配权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "", required = true, paramType = "form")
    })
    @GetMapping("/users")
    public ResponseMessage<List<PhenixAuthority>> findAuthorityUser(
            @RequestParam(value = "userId") Long userId
    ) {
        SysUser sysUser = userService.getUserById(userId);
        List<PhenixAuthority> result = authorityService.findAuthorityByUser(userId, CommonConstants.ROOT.equals(sysUser.getUserName()));
        return ResponseMessage.ok(result);
    }


    /**
     * 获取应用已分配接口权限
     *
     * @param appId 角色ID
     * @return
     */
    @ApiOperation(value = "获取应用已分配接口权限", notes = "获取应用已分配接口权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用Id", defaultValue = "", required = true, paramType = "form")
    })
    @GetMapping("/apps")
    public ResponseMessage<List<PhenixAuthority>> findAuthorityApp(
            @RequestParam(value = "appId") Long appId
    ) {
        List<PhenixAuthority> result = authorityService.findAuthorityByApp(appId);
        return ResponseMessage.ok(result);
    }

    /**
     * 分配角色权限
     *
     * @param authorityRoleParameter
     * @return
     */
    @ApiOperation(value = "分配角色权限", notes = "分配角色权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "expireTime", value = "过期时间.选填", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", value = "权限ID.多个以,隔开.选填", defaultValue = "", required = false, paramType = "form")
    })
    @PostMapping("/role/grant")
    public ResponseMessage grantAuthorityRole(@RequestBody AuthorityRoleParameter authorityRoleParameter) {
        authorityService.addAuthorityRole(authorityRoleParameter.getRoleId(), authorityRoleParameter.getExpireTime(),
                authorityRoleParameter.getAuthorityIds());
        dingJustRestTemplate.refreshGateway();
        return ResponseMessage.ok();
    }


    /**
     * 分配用户权限
     *
     * @param authorityUserParameter
     * @return
     */
    @ApiOperation(value = "分配用户权限", notes = "分配用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "expireTime", value = "过期时间.选填", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", value = "权限ID.多个以,隔开.选填", defaultValue = "", required = false, paramType = "form")
    })
    @PostMapping("/user/grant")
    public ResponseMessage grantAuthorityUser(@RequestBody AuthorityUserParameter authorityUserParameter) {
        authorityService.addAuthorityUser(authorityUserParameter.getUserId(), authorityUserParameter.getExpireTime(), authorityUserParameter.getAuthorityIds());
        dingJustRestTemplate.refreshGateway();
        return ResponseMessage.ok();
    }


    /**
     * 分配应用权限
     *
     * @param authorityAppParameter
     * @return
     */
    @ApiOperation(value = "分配应用权限", notes = "分配应用权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用Id", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "expireTime", value = "过期时间.选填", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", value = "权限ID.多个以,隔开.选填", defaultValue = "", required = false, paramType = "form")
    })
    @PostMapping("/app/grant")
    public ResponseMessage grantAuthorityApp(@RequestBody AuthorityAppParameter authorityAppParameter) {
        authorityService.addAuthorityApp(authorityAppParameter.getAppId(), authorityAppParameter.getExpireTime(),
                authorityAppParameter.getAuthorityIds());
        dingJustRestTemplate.refreshGateway();
        return ResponseMessage.ok();
    }

    /**
     * 功能按钮绑定API
     *
     * @param authorityActionParameter
     * @return
     */
    @ApiOperation(value = "功能按钮授权", notes = "功能按钮授权")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actionId", required = true, value = "功能按钮ID", paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", required = false, value = "全新ID:多个用,号隔开", paramType = "form"),
    })
    @PostMapping("/action/grant")
    public ResponseMessage grantAuthorityAction(@RequestParam AuthorityActionParameter authorityActionParameter
    ) {
        authorityService.addAuthorityAction(authorityActionParameter.getActionId(), authorityActionParameter.getAuthorityIds());
        dingJustRestTemplate.refreshGateway();
        return ResponseMessage.ok();
    }


}
