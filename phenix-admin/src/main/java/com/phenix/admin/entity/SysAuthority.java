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
    * 系统权限-菜单权限、操作权限、API权限
    * </p>
 *
 * @author zhenghui
 * @since 2019-12-18
 */
@TableName("sys_authority")
@ApiModel(value="Authority对象", description="系统权限-菜单权限、操作权限、API权限")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SysAuthority implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "authority_id", type = IdType.ASSIGN_ID)
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonSerialize(using = CustomLongSerializer.class)
    private Long authorityId;

    @ApiModelProperty(value = "权限类型，1：菜单权限，2：api权限")
    private Integer authority_type;

    @ApiModelProperty(value = "权限标识")
    private String authority;

    @ApiModelProperty(value = "菜单资源ID")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonSerialize(using = CustomLongSerializer.class)
    private Long menuId;

    @ApiModelProperty(value = "API资源ID")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonSerialize(using = CustomLongSerializer.class)
    private Long apiId;

    @ApiModelProperty(value = "动作id")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonSerialize(using = CustomLongSerializer.class)
    private Long actionId;

    @ApiModelProperty(value = "状态")
    private Integer status;

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

    @ApiModelProperty(value = "修改时间")
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
