package com.itheima.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Schema(description = "分类分页查询参数")
public class OrdersPageQueryDTO implements Serializable {

    @Schema(description = "订单号（可选，支持模糊查询）")
    private String number;

    @Schema(description = "手机号（可选）")
    private String phone;

    @Schema(description = "订单状态 0-待接单 1-待派送 2-派送中 3-已完成 4-已取消（可选）")
    private Integer status;

    @Schema(description = "页码")
    private Integer page;

    @Schema(description = "每页显示记录数")
    private Integer pageSize;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "下单开始日期（可选）")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "下单结束日期（可选）")
    private LocalDate endDate;

}
