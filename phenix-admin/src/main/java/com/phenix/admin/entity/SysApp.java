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
    * 系统应用-基础信息
    * </p>
 *
 * @author zhenghui
 * @since 2019-12-18
 */
@TableName("sys_app")
@ApiModel(value="App对象", description="系统应用-基础信息")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SysApp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客户端ID")
    @TableId(value = "app_id", type = IdType.ASSIGN_ID)
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonSerialize(using = CustomLongSerializer.class)
    private Long appId;

    @ApiModelProperty(value = "API访问key")
    private String apiKey;

    @ApiModelProperty(value = "API访问密钥")
    private String secretKey;

    @ApiModelProperty(value = "app名称")
    private String appName;

    @ApiModelProperty(value = "app英文名称")
    private String appNameEn;

    @ApiModelProperty(value = "应用图标")
    private String appIcon;

    @ApiModelProperty(value = "app类型:server-服务应用 app-手机应用 pc-PC网页应用 wap-手机网页应用")
    private String appType;

    @ApiModelProperty(value = "app描述")
    private String appDesc;

    @ApiModelProperty(value = "移动应用操作系统:ios-苹果 android-安卓")
    private String appOs;

    @ApiModelProperty(value = "官网地址")
    private String website;

    @ApiModelProperty(value = "状态:0-无效 1-有效")
    private Integer status;

    @ApiModelProperty(value = "保留数据0-否 1-是 不允许删除")
    private Boolean isPersist;

    @ApiModelProperty(value = "创建用户id")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonSerialize(using = CustomLongSerializer.class)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
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
