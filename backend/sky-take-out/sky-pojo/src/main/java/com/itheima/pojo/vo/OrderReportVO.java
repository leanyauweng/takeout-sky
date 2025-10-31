package com.itheima.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderReportVO implements Serializable {
    private String dateList;
    private String orderCountList; //每日订单数
    private String validOrderCountList; //每日有效订单数
    private Integer totalOrderCount; //订单总数
    private Integer validOrderCount; //有效订单数
    private Double orderCompletionRate; // 订单完成率
}
