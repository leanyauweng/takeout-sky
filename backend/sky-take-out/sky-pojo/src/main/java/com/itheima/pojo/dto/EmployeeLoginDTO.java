package com.itheima.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 员工登录请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "员工登录请求参数")
public class EmployeeLoginDTO implements Serializable {
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    private String password;
}
