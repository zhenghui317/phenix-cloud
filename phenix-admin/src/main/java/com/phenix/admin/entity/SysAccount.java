package com.phenix.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.phenix.starter.web.common.serializer.CustomLongDeserializer;
import com.phenix.starter.web.common.serializer.CustomLongSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
    * 登录账号
    * </p>
 *
 * @author zhenghui
 * @since 2019-12-18
 */
@TableName("sys_account")
@ApiModel(value="Account对象", description="登录账号")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SysAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "account_id", type = IdType.ASSIGN_ID)
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonSerialize(using = CustomLongSerializer.class)
    private Long accountId;

    @ApiModelProperty(value = "租户id")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonSerialize(using = CustomLongSerializer.class)
    private Long tenantId;

    @ApiModelProperty(value = "用户Id")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonSerialize(using = CustomLongSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "标识：手机号、邮箱、 用户名、或第三方应用的唯一标识")
    private String account;

    @ApiModelProperty(value = "密码凭证：站内的保存密码、站外的不保存或保存token）")
    private String password;

    @ApiModelProperty(value = "登录类型:password-密码、mobile-手机号、email-邮箱、weixin-微信、weibo-微博、qq-等等")
    private String accountType;

    @ApiModelProperty(value = "账户域:@admin.com,@developer.com")
    private String domain;

    @ApiModelProperty(value = "注册IP")
    private String registerIp;

    @ApiModelProperty(value = "状态:0-禁用 1-启用 2-锁定")
    private Integer status;

    @ApiModelProperty(value = "帐号过期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "创建用户id")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonSerialize(using = CustomLongSerializer.class)
    private Long createUserId;

    @ApiModelProperty(value = "注册时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
	@TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新用户id")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonSerialize(using = CustomLongSerializer.class)
    private Long updateUserId;

    @ApiModelProperty(value = "更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
	@TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否逻辑删除，0:未删除；1:已删除")
    @TableLogic
    private Boolean isDelete;


}
