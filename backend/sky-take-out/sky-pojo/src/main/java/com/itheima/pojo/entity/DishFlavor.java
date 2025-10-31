package com.itheima.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishFlavor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;        // 主键
    private Long dishId;    // 菜品id（关联dish表）
    private String name;    // 口味名称
    private String value;   // 口味值
}
