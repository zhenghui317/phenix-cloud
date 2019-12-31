package com.phenix.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.google.common.collect.Lists;
import com.phenix.starter.mybatisplus.generator.MybatisPlusCodeGenerator;
import com.phenix.starter.mybatisplus.parameter.GenerateCodeParameter;

import java.util.List;

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {
    /**
     * 作者
     */
    private static String author = "zhenghui";
    /**
     * 命名空间
     */
    private static String nameSpace = "com.phenix.admin.codegenerator";
    /**
     * 数据源
     */
    private static String dataSource = "jdbc:mysql://localhost:3306/phenix_admin?characterEncoding=utf-8";

    public static void main(String[] args) {
        List<String> tablesList = Lists.newArrayList("sys_dict_data", "sys_dict_type", "sys_dept", "sys_config");
        GenerateCodeParameter generateCodeParameter = GenerateCodeParameter.builder()
                .author(author)
                .nameSpace(nameSpace)
                .dataSource(dataSource)
                .dataUser("root")
                .dataPwd("root")
                .idType(IdType.ASSIGN_ID)
                .fileOverride(false)
                .includeTableList(tablesList)
                .build();
        MybatisPlusCodeGenerator mybatisPlusCodeGenerator = new MybatisPlusCodeGenerator();
        mybatisPlusCodeGenerator.generate(generateCodeParameter);
    }


}
