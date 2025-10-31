package com.itheima.pojo.vo;

import com.itheima.pojo.entity.SetmealDish;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SetmealVO implements Serializable {
    private Long id;
    private String name;
    private Long categoryId;
    private BigDecimal price;
    private String description;
    private String image;
    private Integer status;
    private LocalDateTime updateTime;

    private String categoryName; // 分类名称（关联 category 表时用
    // 🔹 返回套餐详情时，带上关联菜品信息
    private List<SetmealDish> setmealDishes;
}
