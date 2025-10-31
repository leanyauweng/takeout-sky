package com.itheima.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category implements Serializable {
    private Long id;            // 主键
    private String name;        // 分类名称
    private Integer type;       // 分类类型
    private Integer sort;       // 排序字段
    private Integer status;     // 状态
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 最后修改时间
    private Long createUser;    // 创建人id
    private Long updateUser;    // 最后修改人id
}
