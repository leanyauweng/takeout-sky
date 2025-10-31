package com.itheima.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 工作台 - 订单总览数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderOverViewVO implements Serializable {

    /** 待接单数 */
    private Integer waitingOrders;

    /** 已接单数 */
    private Integer deliveredOrders;

    /** 已完成数 */
    private Integer completedOrders;

    /** 已取消数 */
    private Integer cancelledOrders;

    /** 总订单数 */
    private Integer allOrders;
}
