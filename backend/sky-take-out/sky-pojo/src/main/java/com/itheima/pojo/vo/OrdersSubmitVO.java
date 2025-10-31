package com.itheima.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrdersSubmitVO {
    private Long id;                        //订单id
    private String orderNumber;              //订单号
    private BigDecimal orderAmount;              //订单金额
    private LocalDateTime orderTime;              //下单时间
}
