package com.itheima.controller.admin;

import com.itheima.pojo.dto.OrderDTO;
import com.itheima.pojo.dto.OrdersPageQueryDTO;
import com.itheima.pojo.entity.Orders;
import com.itheima.result.PageResult;
import com.itheima.result.Result;
import com.itheima.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/order")
@Tag(name = "服务端订单相关接口")
@Slf4j
public class adminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/page")
    @Operation(summary = "分页查询订单列表")
    public Result<PageResult> page(
            @RequestParam(required = false) String number,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        OrdersPageQueryDTO dto = new OrdersPageQueryDTO();
        dto.setNumber(number);
        dto.setPhone(phone);
        dto.setStatus(status);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        dto.setPage(page);
        dto.setPageSize(pageSize);

        PageResult pageResult = orderService.pageQuery(dto);
        return Result.success(pageResult);
    }


    @PostMapping("/accept/{id}")
    @Operation(summary = "商家接单")
    public Result acceptOrder(@PathVariable Long id) {
        orderService.acceptOrder(id);
        return Result.success();
    }

    @PostMapping("/deliver/{id}")
    @Operation(summary = "派送订单")
    public Result deliverOrder(@PathVariable Long id) {
        orderService.deliverOrder(id);
        return Result.success();
    }

    @PostMapping("/complete/{id}")
    @Operation(summary = "确认完成订单")
    public Result completeOrder(@PathVariable Long id) {
        orderService.completeOrder(id);
        return Result.success();
    }

    @PostMapping("/reject/{id}")
    @Operation(summary = "商家拒单")
    public Result rejectOrder(@PathVariable Long id) {
        orderService.rejectOrder(id);
        return Result.success();
    }

    @PostMapping("/cancel/{id}")
    @Operation(summary = "商家取消")
    public Result cancelOrder(@PathVariable Long id) {
        orderService.adminCencelOrder(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据订单ID查看详情")
    public Result<Orders> getOrderDetail(@PathVariable Long id) {
        Orders order = orderService.getOrderWithDetails(id);
        return Result.success(order);
    }
}
