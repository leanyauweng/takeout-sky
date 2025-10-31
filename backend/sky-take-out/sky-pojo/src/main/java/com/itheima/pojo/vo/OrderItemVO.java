package com.itheima.pojo.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 订单明细返回对象（VO）
 * 用于前端展示订单中每个菜品/套餐的详细信息
 */
@Data
public class OrderItemVO {
    private Long id;           // 明细id
    private Long orderId;      // 订单id
    private Long dishId;       // 菜品id
    private Long setmealId;    // 套餐id
    private String name;       // 商品名称
    private String image;      // 商品图片
    private String dishFlavor; // 菜品口味
    private Integer number;    // 数量
    private BigDecimal amount; // 单价
}
