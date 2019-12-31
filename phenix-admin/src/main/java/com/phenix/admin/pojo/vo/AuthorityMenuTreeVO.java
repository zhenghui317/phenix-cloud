package com.phenix.admin.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 前端树形菜单结构
 *
 * @author zhenghui
 * @date 2019-12-22
 */
@Data
public class AuthorityMenuTreeVO {
    /**
     * 根菜单集合
     */
    @ApiModelProperty(value = "当前菜单的子菜单")
    private List<AuthorityMenuVO> children;
}
