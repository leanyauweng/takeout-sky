package com.itheima.controller.user;

import com.itheima.constant.JwtClaimsConstant;
import com.itheima.pojo.dto.UserLoginDTO;
import com.itheima.pojo.entity.User;
import com.itheima.pojo.vo.UserLoginVO;
import com.itheima.properties.JwtProperties;
import com.itheima.result.Result;
import com.itheima.service.UserService;
import com.itheima.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Tag(name = "用户登入相关接口")
@Slf4j
public class UserController {

    @Autowired private UserService userService;

    @Autowired private JwtProperties jwtProperties;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("用户登入：{}",userLoginDTO);

        User user = userService.login(userLoginDTO);

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .authentication(token)
                .build();
        return Result.success(userLoginVO);
    }
}
