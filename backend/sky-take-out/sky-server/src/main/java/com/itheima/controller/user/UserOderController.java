package com.itheima.controller.user;

import com.itheima.context.BaseContext;
import com.itheima.pojo.dto.OrdersSubmitDTO;
import com.itheima.pojo.entity.Orders;
import com.itheima.pojo.vo.OrderDetailVO;
import com.itheima.pojo.vo.OrderVO;
import com.itheima.pojo.vo.OrdersSubmitVO;
import com.itheima.result.Result;
import com.itheima.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/user/order")
@Tag(name = "用户端订单相关接口")
@Slf4j
public class UserOderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    @Operation(summary = "用户下单")
    public Result<OrdersSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单参数为：{}",ordersSubmitDTO);
        OrdersSubmitVO ordersSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(ordersSubmitVO);
    }

    @GetMapping("/reminder/{id}")
    @Operation(summary = "客户催单")
    public Result reminder(@PathVariable Long id){
        orderService.reminder(id);
        return Result.success();
    }

    @GetMapping("/reorder/{orderId}")
    @Operation(summary = "再来一单")
    public Result<OrdersSubmitVO> reorder(@PathVariable Long orderId){
        OrdersSubmitVO ordersSubmitVO = orderService.reorder(orderId);
        return Result.success(ordersSubmitVO);
    }

    @PostMapping("/cancel/{id}")
    @Operation(summary = "取消订单")
    public Result cancelOrder(@PathVariable Long id, @RequestParam(required = false) String reason) {
        orderService.cancelOrder(id, reason == null ? "用户主动取消" : reason);
        return Result.success();
    }

    /**
     * 根据订单号查询订单详情
     */
    @GetMapping("/detail/{orderNumber}")
    @Operation(summary = "根据订单号查询订单详情")
    public Result<OrderDetailVO> getOrderDetail(@PathVariable String orderNumber) {
        log.info("查询订单详情，orderNumber={}", orderNumber);

        OrderDetailVO orderDetail = orderService.getOrderByNumber(orderNumber);

        return Result.success(orderDetail);
    }

    @GetMapping("/list")
    @Operation(summary = "根据用户ID查询全部订单")
    public Result<List<OrderVO>> list() {
        Long userId = BaseContext.getCurrentId(); // 从当前登录上下文获取用户id
        List<OrderVO> list = orderService.listByUserId(userId);
        return Result.success(list);
    }
}
