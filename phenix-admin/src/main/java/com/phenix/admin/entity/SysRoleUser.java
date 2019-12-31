package com.phenix.admin.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
    * 系统角色-用户关联
    * </p>
 *
 * @author zhenghui
 * @since 2019-12-18
 */
@TableName("sys_role_user")
@ApiModel(value="RoleUser对象", description="系统角色-用户关联")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonSerialize(using = CustomLongSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "角色ID")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonSerialize(using = CustomLongSerializer.class)
    private Long roleId;

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


}
