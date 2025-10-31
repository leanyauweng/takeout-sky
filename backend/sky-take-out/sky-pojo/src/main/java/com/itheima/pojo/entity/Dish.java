package com.itheima.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dish {

    private Long id;                 // 主键
    private String name;             // 菜品名称
    private Long categoryId;         // 分类id（逻辑外键，指向分类表）
    private BigDecimal price;        // 菜品价格
    private String image;            // 图片路径
    private String description;      // 菜品描述
    private Integer status;          // 售卖状态：1起售 0停售
    private LocalDateTime createTime;// 创建时间
    private LocalDateTime updateTime;// 最后修改时间
    private Long createUser;         // 创建人id
    private Long updateUser;         // 最后修改人id

}
