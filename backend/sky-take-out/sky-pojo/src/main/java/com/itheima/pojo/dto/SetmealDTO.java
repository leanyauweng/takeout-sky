package com.itheima.pojo.dto;

import com.itheima.pojo.entity.SetmealDish;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetmealDTO implements Serializable {
    private Long id;                 // 主键（修改时才有）
    private String name;             // 套餐名称
    private Long categoryId;         // 分类id
    private BigDecimal price;         // 套餐价格
    private String description;       // 套餐描述
    private String image;             // 套餐图片
    private Integer status;          // 状态 1起售 0停售

    // 🔹 关联的菜品列表（额外字段）
    private List<SetmealDish> setmealDishes;

}
