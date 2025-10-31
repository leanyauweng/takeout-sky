package com.itheima.pojo.vo;

import com.itheima.pojo.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DishVO {

    private Long id;                 // 主键
    private String name;             // 菜品名称
    private Long categoryId;         // 分类id（逻辑外键，指向分类表）
    private BigDecimal price;        // 菜品价格
    private String image;            // 图片路径
    private String description;      // 菜品描述
    private Integer status;          // 售卖状态：1起售 0停售
    private LocalDateTime updateTime;// 最后修改时间
    private String categoryName;
    private List<DishFlavor> flavors = new ArrayList<>();
}
