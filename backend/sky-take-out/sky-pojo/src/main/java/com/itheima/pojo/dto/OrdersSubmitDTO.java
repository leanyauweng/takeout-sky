package com.itheima.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrdersSubmitDTO implements Serializable {

    private Long addressBookId;           // 地址ID
    private String remark;                // 备注
    private Integer tablewareNumber;      //餐具数量
    private Integer packAmount;           //打包费
}
