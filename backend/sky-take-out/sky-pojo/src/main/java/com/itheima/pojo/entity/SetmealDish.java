package com.itheima.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SetmealDish implements Serializable {
    private Long id;          // 主键
    private Long setmealId;   // 套餐id
    private Long dishId;      // 菜品id
    private String name;      // 菜品名称（冗余）
    private BigDecimal price; // 菜品价格（冗余）
    private Integer copies;   // 份数
    private Integer status;   // 菜品状态（通过 join dish 表查询）
}
