package com.itheima.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVO {
    private Long id;
    private String number;
    private BigDecimal amount;
    private Integer status;
    private LocalDateTime createTime;

    // 可选：嵌套餐品信息
    private List<OrderItemVO> items;
}
