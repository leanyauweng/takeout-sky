package com.itheima.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "分类返回的数据格式")
public class CategoryVO implements Serializable {

    @Schema(description = "主键值")
    private Long id;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "分类类型 1-菜品分类 2-套餐分类")
    private Integer type;

    @Schema(description = "排序字段")
    private Integer sort;

    @Schema(description = "状态 1-启用 0-禁用")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "最后修改时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建人id")
    private Long createUser;

    @Schema(description = "最后修改人id")
    private Long updateUser;
}