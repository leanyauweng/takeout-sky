package com.itheima.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户登录请求参数")
public class UserLoginDTO implements Serializable {
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "密码")
    private String password;
}
