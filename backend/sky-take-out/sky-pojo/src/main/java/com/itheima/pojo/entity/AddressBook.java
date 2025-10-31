package com.itheima.pojo.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddressBook {
    private Long id;
    private Long userId;
    private String consignee;
    private String sex;
    private String phone;
    private String stateName;
    private String cityName;
    private String postcode;
    private String detail;
    private String label;
    private Integer isDefault;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
