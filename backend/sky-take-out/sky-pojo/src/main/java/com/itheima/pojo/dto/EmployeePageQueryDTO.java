package com.itheima.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "分类分页查询参数")
public class EmployeePageQueryDTO implements Serializable {

    private String name;

    private int page;

    private int pageSize;

}
