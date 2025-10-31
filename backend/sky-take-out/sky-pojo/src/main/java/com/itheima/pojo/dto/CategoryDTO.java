package com.itheima.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryDTO implements Serializable {
    private Long id;
    private String name;   // 分类名称
    private Integer type;  // 分类类型：1 菜品分类，2 套餐分类
    private Integer sort;  // 排序字段
    private Integer status; // 状态：1 启用，0 禁用
}
