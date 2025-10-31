package com.itheima.controller.user;

import com.itheima.context.BaseContext;
import com.itheima.pojo.entity.Orders;
import com.itheima.result.Result;
import com.itheima.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/user/payment")
@Tag(name = "用户端付款相关接口")
@Slf4j
public class UserPaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    @Operation(summary = "用户付款")
    public Result pay(@RequestParam String orderNumber) {
        Long userId = BaseContext.getCurrentId();
        log.info("查询订单参数 number={}, userId={}", orderNumber, userId);

        paymentService.payOrder(orderNumber, userId);

        return Result.success();
    }
}
