package com.phenix.admin.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.phenix.starter.web.common.serializer.CustomLongDeserializer;
import com.phenix.starter.web.common.serializer.CustomLongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色DTO
 */
@Data
public class RoleDTO {

    @TableId(value = "role_id", type = IdType.ASSIGN_ID)
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonSerialize(using = CustomLongSerializer.class)
    private Long roleId;

    @ApiModelProperty(value = "角色编码")
    private String roleCode;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色描述")
    private String roleDesc;
}
