package com.phenix.admin.controller;

import com.phenix.admin.entity.SysUser;
import com.phenix.admin.pojo.parameter.UpdatePasswordParameter;
import com.phenix.admin.pojo.parameter.UpdateUserInfoParameter;
import com.phenix.admin.pojo.vo.AuthorityMenuTreeVO;
import com.phenix.admin.service.ISysAuthorityService;
import com.phenix.admin.service.ISysUserService;
import com.phenix.core.security.PhenixOAuthHelper;
import com.phenix.core.security.PhenixUserDetails;
import com.phenix.core.security.token.store.PhenixRedisTokenStore;
import com.phenix.defines.constants.CommonConstants;
import com.phenix.defines.response.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author: zhenghui
 * @date: 2019/5/24 13:31
 * @description:
 */
@Slf4j
@SuppressWarnings("unchecked")
@Api(tags = "当前登陆用户")
@RestController
public class CurrentUserController {

    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysAuthorityService authorityService;
    @Autowired
    private PhenixRedisTokenStore redisTokenStore;


    /**
     * 获取用户基础信息
     *
     * @return
     */
    @ApiOperation(value = "获取当前登录用户信息", notes = "获取当前登录用户信息")
    @GetMapping("/current/user/info")
    public ResponseMessage getUserProfile() {
        return ResponseMessage.ok(PhenixOAuthHelper.getUser());
    }


    /**
     * 修改当前登录用户密码
     *
     * @return
     */
    @ApiOperation(value = "修改当前登录用户密码", notes = "修改当前登录用户密码")
    @GetMapping(value = "/current/user/rest/password", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage restPassword(@RequestBody UpdatePasswordParameter updatePasswordParameter) {
        userService.updatePassword(PhenixOAuthHelper.getUser().getUserId(), updatePasswordParameter.getPassword());
        return ResponseMessage.ok();
    }

    /**
     * 修改当前登录用户基本信息
     *
     * @param updateUserInfoParameter
     * @return
     */
    @ApiOperation(value = "修改当前登录用户基本信息", notes = "修改当前登录用户基本信息")
    @PostMapping(value = "/current/user/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage updateUserInfo(
            @RequestBody UpdateUserInfoParameter updateUserInfoParameter
    ) {
        PhenixUserDetails openUserDetails = PhenixOAuthHelper.getUser();
        SysUser sysUser = new SysUser();
        sysUser.setUserId(openUserDetails.getUserId());
        sysUser.setNickName(updateUserInfoParameter.getNickName());
        sysUser.setUserDesc(updateUserInfoParameter.getUserDesc());
        sysUser.setAvatar(updateUserInfoParameter.getAvatar());
        userService.updateUser(sysUser);
        openUserDetails.setNickName(updateUserInfoParameter.getNickName());
        openUserDetails.setAvatar(updateUserInfoParameter.getAvatar());
        PhenixOAuthHelper.updatePhenixUser(redisTokenStore, openUserDetails);
        return ResponseMessage.ok();
    }

    /**
     * 获取登陆用户已分配权限
     *
     * @return
     */
    @ApiOperation(value = "获取当前登录用户已分配菜单权限", notes = "获取当前登录用户已分配菜单权限")
    @GetMapping(value = "/current/user/menu", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<AuthorityMenuTreeVO> findAuthorityMenu() {
        AuthorityMenuTreeVO result = authorityService.findAuthorityMenuTreeByUser(
                PhenixOAuthHelper.getUser().getUserId()
                , CommonConstants.ROOT.equals(PhenixOAuthHelper.getUser().getUsername()));
        return ResponseMessage.ok(result);
    }
}
