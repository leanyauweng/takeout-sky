package com.itheima.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DishTop10VO {
    private Long id;
    private String name;
    private String image;
    private BigDecimal price;
    private Integer sales;
}
