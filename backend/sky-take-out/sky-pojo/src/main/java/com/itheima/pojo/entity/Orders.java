package com.itheima.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders implements Serializable {

    public static final Integer PENDING_PAYMENT = 1;
    public static final Integer TO_BE_CONFIRMED = 2;
    public static final Integer CONFIRMED = 3;
    public static final Integer DELIVERY_IN_PROGRESS = 4;
    public static final Integer COMPLETED = 5;
    public static final Integer CANCELLED = 6;

    public static final Integer UN_PAID = 0;
    public static final Integer PAID = 1;
    public static final Integer REFUND = 2;

    public static final long serialVersionUID = 1L;

    private Long id;                         // 主键
    private Long userId;                     // 用户ID
    private Long addressBookId;              // 地址ID
    private String number;                   // 订单号
    private String consignee;                // 收货人
    private String phone;                    // 电话
    private String address;                  // 地址
    private Integer status;                  // 订单状态
    private String cancelReason;             // 取消原因
    private String rejectionReason;          // 拒单原因
    private LocalDateTime cancelTime;        // 取消时间
    private LocalDateTime orderTime;         // 下单时间
    private LocalDateTime estimatedDeliveryTime; // 预计送达时间
    private LocalDateTime checkoutTime;      // 付款时间
    private Integer deliveryStatus;          // 配送状态
    private Integer payMethod;               // 支付方式
    private LocalDateTime deliveryTime;      // 送达时间
    private Integer payStatus;               // 支付状态
    private Integer packAmount;              // 打包费
    private BigDecimal amount;               // 订单金额
    private Integer tablewareNumber;         // 餐具数量
    private String remark;                   // 备注
    private Integer tablewareStatus;         // 餐具数量状态
    private LocalDateTime createTime;        // 创建时间
    private LocalDateTime updateTime;        // 修改时间

    private List<OrderDetail> orderDetails;
}
