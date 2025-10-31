package com.itheima.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDetailVO {
    private Long id;
    private String orderNumber;
    private BigDecimal amount;
    private Integer status;
    private LocalDateTime orderTime;
}
