package com.itheima.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单明细表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail implements Serializable {

    private Long id;                 // 主键
    private Long orderId;            // 订单id（逻辑外键）
    private Long dishId;             // 菜品id（逻辑外键）
    private Long setmealId;          // 套餐id（逻辑外键）
    private String dishFlavor;       // 菜品口味
    private String name;             // 商品名称（冗余字段）
    private String image;            // 商品图片路径（冗余字段）
    private Integer number;          // 商品数量
    private BigDecimal amount;       // 商品单价
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 修改时间
}
