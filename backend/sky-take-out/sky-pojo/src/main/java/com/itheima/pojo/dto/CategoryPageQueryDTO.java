package com.itheima.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Schema(description = "分类分页查询参数")
public class CategoryPageQueryDTO implements Serializable {

    @Schema(description = "分类名称（可选，支持模糊查询）")
    private String name;

    @Schema(description = "分类类型 1-菜品分类 2-套餐分类（可选）")
    private Integer type;

    @Schema(description = "页码")
    private Integer page;

    @Schema(description = "每页显示记录数")
    private Integer pageSize;
}
