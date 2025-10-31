package com.itheima.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO implements Serializable {

    @Schema(description = "主键值")
    private Long id;           // 用户ID

    @Schema(description = "JWT令牌")
    private String authentication;      // 登录成功后生成的 JWT token


}
