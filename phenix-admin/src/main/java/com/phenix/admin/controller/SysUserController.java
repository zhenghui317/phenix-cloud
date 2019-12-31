package com.phenix.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phenix.admin.entity.SysRole;
import com.phenix.admin.entity.SysUser;
import com.phenix.admin.pojo.dto.SysUserDTO;
import com.phenix.admin.pojo.parameter.ThirdPartyParameter;
import com.phenix.admin.pojo.parameter.UpdatePasswordParameter;
import com.phenix.admin.pojo.parameter.UserRolesParameter;
import com.phenix.admin.service.ISysRoleService;
import com.phenix.admin.service.ISysUserService;
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
 * 系统用户信息
 *
 * @author zhenghui
 */
@Slf4j
@SuppressWarnings("unchecked")
@Api(tags = "系统用户管理")
@RestController
@RequestMapping("/user")
public class SysUserController {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysRoleService roleService;


    @ApiOperation(value = "获取数据", notes = "获取单个数据")
    @ApiImplicitParam(value = "主键", name = "userId", required = true)
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<SysUser> get(@RequestParam Long userId) {
        SysUser entity = userService.getById(userId);
        if(entity != null){
            return ResponseMessage.ok(entity);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "删除数据", notes = "删除单个数据")
    @ApiImplicitParam(value = "主键", name = "userId", required = true)
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage delete(@RequestParam Long userId){
        Boolean ok = userService.removeById(userId);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "新增数据", notes = "新增数据")
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage save(@RequestBody SysUser sysUser) {
        Boolean ok = userService.save(sysUser);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "更新数据", notes = "根据主键更新数据")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage update(@RequestBody SysUser sysUser){
        Boolean ok = userService.updateUser(sysUser);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "保存或更新数据", notes = "保存或更新数据")
    @PostMapping(value = "/saveOrUpdate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveOrUpdate(@RequestBody SysUser sysUser){
        Boolean ok = userService.saveOrUpdate(sysUser);
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
    public ResponseMessage<IPage<SysUser>> page(@RequestParam Integer page, @RequestParam Integer size, SysUser entity){
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>(entity);
        IPage<SysUser> respIPage = userService.page(new Page(page,size), wrapper);
        if(respIPage != null){
            return ResponseMessage.ok(respIPage);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "查询数据集合", notes = "查询数据集合")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<List<SysUser>> list(SysUser entity){
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>(entity);
        List<SysUser> list = userService.list(wrapper);
        if(list != null){
            return ResponseMessage.ok(list);
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量新增数据", notes = "批量新增数据")
    @PostMapping(value = "/saveBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage saveBatch(@RequestBody List<SysUser> sysUsers) {
        Boolean ok = userService.saveBatch(sysUsers);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量更新数据", notes = "批量更新数据")
    @PutMapping(value = "/updateBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage updateBatch(@RequestBody List<SysUser> sysUsers) {
        Boolean ok = userService.saveOrUpdateBatch(sysUsers);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    @ApiOperation(value = "批量删除数据", notes = "批量删除数据")
    @PostMapping(value = "/deleteBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage deleteBatch(@RequestBody List<Long> userIds) {
        Boolean ok = userService.removeByIds(userIds);
        if(ok){
            return ResponseMessage.ok();
        }
        return ResponseMessage.fail();
    }

    /**
     * 获取所有用户列表
     *
     * @return
     */
    @ApiOperation(value = "获取所有用户列表", notes = "获取所有用户列表")
    @GetMapping("/all")
    public ResponseMessage<List<SysRole>> getUserAllList() {
        return ResponseMessage.ok(userService.findAllList());
    }

    /**
     * 添加系统用户
     *
     * @param userDTO
     * @return
     */
    @ApiOperation(value = "添加系统用户", notes = "添加系统用户")
    @PostMapping("/addUser")
    public ResponseMessage<Long> addUser(@RequestBody SysUserDTO userDTO) {
        userService.addUser(userDTO, userDTO.getPassword());
        return ResponseMessage.ok();
    }



    /**
     * 修改用户密码
     *
     * @param updatePasswordParameter
     * @return
     */
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @PostMapping("/update/password")
    public ResponseMessage updatePassword(@RequestBody UpdatePasswordParameter updatePasswordParameter                                          ) {
        userService.updatePassword(updatePasswordParameter.getUserId(), updatePasswordParameter.getPassword());
        return ResponseMessage.ok();
    }

    /**
     * 用户分配角色
     *
     * @param userRolesParameter
     * @return
     */
    @ApiOperation(value = "用户分配角色", notes = "用户分配角色")
    @PostMapping("/roles/add")
    public ResponseMessage addUserRoles(
            @RequestBody UserRolesParameter userRolesParameter
                                        ) {
        roleService.saveUserRoles(userRolesParameter.getUserId(), userRolesParameter.getRoleIds());
        return ResponseMessage.ok();
    }

    /**
     * 获取用户角色
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取用户已分配角色", notes = "获取用户已分配角色")
    @GetMapping("/roles")
    public ResponseMessage<List<SysRole>> getUserRoles(
            @RequestParam(value = "userId") Long userId
    ) {
        return ResponseMessage.ok(roleService.getUserRoles(userId));
    }


    /**
     * 注册第三方系统登录账号
     *
     * @param thirdPartyParameter
     * @return
     */
    @ApiOperation(value = "注册第三方系统登录账号", notes = "仅限系统内部调用")
    @PostMapping("/add/thirdParty")
    public ResponseMessage addUserThirdParty(
            @RequestBody ThirdPartyParameter thirdPartyParameter
    ) {
        SysUser sysUser = new SysUser();
        sysUser.setNickName(thirdPartyParameter.getNickName());
        sysUser.setUserName(thirdPartyParameter.getAccount());
        sysUser.setAvatar(thirdPartyParameter.getAvatar());
        userService.addUserThirdParty(sysUser, thirdPartyParameter.getPassword(), thirdPartyParameter.getPassword());
        return ResponseMessage.ok();
    }

}
