package com.phenix.admin.controller;

import com.phenix.defines.response.ResponseMessage;
import com.phenix.tools.password.PasswordUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordController {
    /**
     * 使用bcrypt方式加密密码
     *
     * @param password 需要加密的密码
     * @return
     */
    @ApiOperation(value = "使用bcrypt方式加密密码", notes = "使用bcrypt方式加密密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", required = true, value = "密码", paramType = "form")
    })
    @GetMapping("/password/bcrypt")
    public ResponseMessage bCryptPasswordEncoder(@RequestParam String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return ResponseMessage.ok(encoder.encode(password));
    }

    /**
     * 使用symmetric方式加密密码
     *
     * @param password 需要加密的密码
     * @return
     */
    @ApiOperation(value = "使用symmetric方式加密密码", notes = "使用symmetric方式加密密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", required = true, value = "密码", paramType = "form")
    })
    @GetMapping("/password/symmetric")
    public ResponseMessage SymmetricPasswordEncoder(@RequestParam String password) {
        String newPassword = PasswordUtils.encryptPassword(password);
        return ResponseMessage.ok(newPassword);
    }
}
