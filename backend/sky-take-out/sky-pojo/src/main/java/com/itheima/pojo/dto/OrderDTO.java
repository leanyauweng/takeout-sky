package com.itheima.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单数据传输对象（OrderDTO）
 */
@Data
public class OrderDTO {
    private Long id;                // 订单ID
    private String status;          // 订单状态: waiting, accepted, delivering, completed, cancelled
    private String orderNo;         // 订单号
    private String dishes;          // 菜品名称，可用逗号分隔多个菜品
    private String address;         // 收货地址
    private String estimatedTime;   // 预计送达时间
    private BigDecimal amount;      // 实收金额
    private String note;            // 备注
}
