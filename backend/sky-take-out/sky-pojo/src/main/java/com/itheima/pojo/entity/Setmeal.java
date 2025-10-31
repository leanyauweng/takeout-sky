package com.itheima.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Setmeal implements Serializable {
    private Long id;          // 主键
    private String name;      // 套餐名称
    private Long categoryId;  // 分类id
    private BigDecimal price; // 套餐价格
    private String description; // 套餐描述
    private String image;     // 套餐图片
    private Integer status;   // 状态：1起售 0停售
}
