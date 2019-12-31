package com.phenix.admin.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.phenix.tools.serializer.CustomLongDeserializer;
import com.phenix.tools.serializer.CustomLongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AuthorityMenuVO {

    @ApiModelProperty(value = "菜单Id")
    @TableId(value = "menu_id", type = IdType.ASSIGN_ID)
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonSerialize(using = CustomLongSerializer.class)
    private Long menuId;

    @ApiModelProperty(value = "父级菜单")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonSerialize(using = CustomLongSerializer.class)
    private Long parentId;

    @ApiModelProperty(value = "菜单编码")
    private String menuCode;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "描述")
    private String menuDesc;

    @ApiModelProperty(value = "路径前缀")
    private String scheme;

    @ApiModelProperty(value = "请求路径")
    private String path;

    @ApiModelProperty(value = "菜单标题")
    private String icon;

    @ApiModelProperty(value = "打开方式:_self窗口内,_blank新窗口")
    private String target;

    @ApiModelProperty(value = "优先级 越小越靠前")
    private Integer priority;

    @ApiModelProperty(value = "当前菜单的子菜单")
    private List<AuthorityMenuVO> children;
}
