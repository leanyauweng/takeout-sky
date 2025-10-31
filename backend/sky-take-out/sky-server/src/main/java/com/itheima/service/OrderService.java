package com.itheima.service;

import com.itheima.pojo.dto.OrderDTO;
import com.itheima.pojo.dto.OrdersPageQueryDTO;
import com.itheima.pojo.dto.OrdersSubmitDTO;
import com.itheima.pojo.entity.Orders;
import com.itheima.pojo.vo.OrderDetailVO;
import com.itheima.pojo.vo.OrderVO;
import com.itheima.pojo.vo.OrdersSubmitVO;
import com.itheima.result.PageResult;
import com.itheima.result.Result;

import java.util.List;

public interface OrderService {
    OrdersSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    void reminder(Long id);

    void acceptOrder(Long id);

    void deliverOrder(Long id);

    void completeOrder(Long id);

    OrdersSubmitVO reorder(Long orderId);

    void cancelOrder(Long id, String s);

    void rejectOrder(Long id);


    Orders getOrderWithDetails(Long id);

    PageResult pageQuery(OrdersPageQueryDTO dto);

    void adminCencelOrder(Long id);

    OrderDetailVO getOrderByNumber(String orderNumber);

    List<OrderVO> listByUserId(Long userId);
}
