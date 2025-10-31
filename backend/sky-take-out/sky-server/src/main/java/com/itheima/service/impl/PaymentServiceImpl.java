package com.itheima.service.impl;

import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.exception.PaymentBusinessException;
import com.itheima.mapper.PaymentMapper;
import com.itheima.pojo.entity.Orders;
import com.itheima.service.PaymentService;
import com.itheima.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public void payOrder(String orderNumber, Long userId) {

        Orders ordersDB = paymentMapper.getByNumberAndUserId(orderNumber, userId);
        if (ordersDB == null){
            throw new PaymentBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if (ordersDB.getPayStatus() == Orders.PAID) {
            throw new PaymentBusinessException(MessageConstant.ORDER_ALREADY_PAY);
        }

        // 模拟支付成功
        ordersDB.setPayStatus(Orders.PAID);
        ordersDB.setStatus(Orders.TO_BE_CONFIRMED);
        ordersDB.setCheckoutTime(LocalDateTime.now());
        ordersDB.setUpdateTime(LocalDateTime.now());

        paymentMapper.update(ordersDB);

        Map map = new HashMap<>();
        map.put("type",1);//1表示来单提醒 2表示客户催单
        map.put("orderId",ordersDB.getId());
        map.put("content",ordersDB.getNumber());

        String json = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);

    }
}
