package com.itheima.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.context.BaseContext;
import com.itheima.exception.AddressBookBusinessException;
import com.itheima.exception.OrderBusinessException;
import com.itheima.exception.ShoppingCartBusinessException;
import com.itheima.mapper.AddressBookMapper;
import com.itheima.mapper.OrderDetailMapper;
import com.itheima.mapper.OrderMapper;
import com.itheima.mapper.ShoppingCartMapper;
import com.itheima.pojo.dto.OrderDTO;
import com.itheima.pojo.dto.OrdersPageQueryDTO;
import com.itheima.pojo.dto.OrdersSubmitDTO;
import com.itheima.pojo.entity.AddressBook;
import com.itheima.pojo.entity.OrderDetail;
import com.itheima.pojo.entity.Orders;
import com.itheima.pojo.entity.ShoppingCart;
import com.itheima.pojo.vo.OrderDetailVO;
import com.itheima.pojo.vo.OrderItemVO;
import com.itheima.pojo.vo.OrderVO;
import com.itheima.pojo.vo.OrdersSubmitVO;
import com.itheima.result.PageResult;
import com.itheima.result.Result;
import com.itheima.service.OrderService;
import com.itheima.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    @Transactional
    public OrdersSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        // 1. 获取用户地址
        AddressBook address = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (address == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        Long userId = BaseContext.getCurrentId();

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);

        if (shoppingCart == null || shoppingCartList.size() == 0){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 3. 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (ShoppingCart cart : shoppingCartList) {
            BigDecimal lineAmount = cart.getAmount().multiply(BigDecimal.valueOf(cart.getNumber()));
            totalAmount = totalAmount.add(lineAmount);
        }

// 4. 加打包费
        if (ordersSubmitDTO.getPackAmount() != null) {
            totalAmount = totalAmount.add(BigDecimal.valueOf(ordersSubmitDTO.getPackAmount()));
        }



        //填充 Orders
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(address.getPhone());
        orders.setAddress(
                (address.getStateName() == null ? "" : address.getStateName()) +
                (address.getCityName() == null ? "" : address.getCityName()) +
                (address.getPostcode() == null ? "" : address.getPostcode()) +
                (address.getDetail() == null ? "" : address.getDetail()));
        orders.setConsignee(address.getConsignee());
        orders.setStatus(1);
        orders.setTablewareStatus(1);
        orders.setPackAmount(ordersSubmitDTO.getPackAmount());
        orders.setRemark(ordersSubmitDTO.getRemark());
        orders.setUserId(userId);
        orders.setAmount(totalAmount);
        orders.setCreateTime(LocalDateTime.now());
        orders.setUpdateTime(LocalDateTime.now());

        // 5. 保存订单
        orderMapper.insert(orders);

        ArrayList<OrderDetail> orderDetailList = new ArrayList<>();

        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);

        shoppingCartMapper.deleteByUserId(userId);

        // 6. 返回 VO
        OrdersSubmitVO odersSubmitvo = new OrdersSubmitVO();
        odersSubmitvo.setId(orders.getId());
        odersSubmitvo.setOrderNumber(orders.getNumber());
        odersSubmitvo.setOrderAmount(orders.getAmount());
        odersSubmitvo.setOrderTime(orders.getOrderTime());

        return odersSubmitvo;


    }

    @Override
    public void reminder(Long id) {
        //根据id查询订单
        Orders ordersDB = orderMapper.getById(id);
        if(ordersDB == null){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Map map = new HashMap();
        map.put("type",2);
        map.put("orderId",ordersDB.getId());
        map.put("content","订单号" + ordersDB.getNumber());

        String json = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);
    }

    @Override
    public void acceptOrder(Long id) {
        Orders ordersDB = orderMapper.getById(id);
        if (ordersDB == null || ordersDB.getStatus().intValue() != Orders.TO_BE_CONFIRMED) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.CONFIRMED);

        orderMapper.update(orders);
    }

    @Override
    public void deliverOrder(Long id) {
        Orders ordersDB = orderMapper.getById(id);
        if (ordersDB == null || ordersDB.getStatus().intValue() != Orders.CONFIRMED) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        orders.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(30));

        orderMapper.update(orders);
    }


    @Override
    public void completeOrder(Long id) {
        Orders ordersDB = orderMapper.getById(id);
        if (ordersDB == null || ordersDB.getStatus().intValue() != Orders.DELIVERY_IN_PROGRESS) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.COMPLETED);
        orders.setCheckoutTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    @Override
    public OrdersSubmitVO reorder(Long orderId) {
        Orders oldOrder = orderMapper.getById(orderId);
        Long userId = BaseContext.getCurrentId();
        if (oldOrder == null ) {
            throw new OrderBusinessException(MessageConstant.O_ORDER_NOT_FOUND);
        }
        if (!oldOrder.getUserId().equals(userId)) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_BELONG_TO_USER);
        }
        if (!oldOrder.getStatus().equals(Orders.COMPLETED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_COMPLETED);
        }

        Orders newOrder = Orders.builder()
                .userId(userId)
                .addressBookId(oldOrder.getAddressBookId())
                .consignee(oldOrder.getConsignee())
                .phone(oldOrder.getPhone())
                .address(oldOrder.getAddress())
                .remark(oldOrder.getRemark())
                .packAmount(oldOrder.getPackAmount())
                .amount(oldOrder.getAmount())
                .tablewareNumber(oldOrder.getTablewareNumber())
                .tablewareStatus(oldOrder.getTablewareStatus())
                .status(Orders.PENDING_PAYMENT)
                .payStatus(Orders.UN_PAID)
                .number(System.currentTimeMillis() + "")
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        orderMapper.insert(newOrder);

        // 生成 VO 返回前端
        OrdersSubmitVO ordersSubmitVO = new OrdersSubmitVO();
        ordersSubmitVO.setId(newOrder.getId());
        ordersSubmitVO.setOrderNumber(newOrder.getNumber());
        ordersSubmitVO.setOrderAmount(newOrder.getAmount());
        ordersSubmitVO.setOrderTime(newOrder.getCreateTime());

        return ordersSubmitVO;
    }

    @Override
    public void cancelOrder(Long id, String reason) {
        // 直接更新订单状态为取消
        int rows = orderMapper.cancelById(id, Orders.CANCELLED, reason);

        if (rows == 0) {
            // 没有更新成功，说明订单状态不允许取消
            throw new OrderBusinessException(MessageConstant.ORDER_CANNOT_CANCEL);
        }
    }


    @Override
    public void rejectOrder(Long id) {
        int rows = orderMapper.rejectById(id, Orders.CANCELLED, "商家拒单");
        if (rows == 0) {
            throw new OrderBusinessException(MessageConstant.ORDER_CANNOT_REJECT);
        }
    }

    @Override
    public void adminCencelOrder(Long id) {
        // 直接更新订单状态为取消
        int rows = orderMapper.admincancelById(id, Orders.CANCELLED,"商家取消");

        if (rows == 0) {
            // 没有更新成功，说明订单状态不允许取消
            throw new OrderBusinessException(MessageConstant.ORDER_CANNOT_CANCEL);
        }
    }

    @Override
    public OrderDetailVO getOrderByNumber(String orderNumber) {
        Orders order = orderMapper.getByNumber(orderNumber);
        if (order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        OrderDetailVO vo = new OrderDetailVO();
        vo.setId(order.getId());
        vo.setOrderNumber(order.getNumber());
        vo.setAmount(order.getAmount());
        vo.setStatus(order.getStatus());
        vo.setOrderTime(order.getOrderTime());
        return vo;
    }

    @Override
    public List<OrderVO> listByUserId(Long userId) {
        List<OrderVO> orders = orderMapper.findByUserId(userId);
        for (OrderVO order : orders) {
            List<OrderItemVO> items = orderDetailMapper.findByOrderId(order.getId());
            order.setItems(items);
        }
        return orders;
    }

    @Override
    public Orders getOrderWithDetails(Long id) {
        Orders order = orderMapper.selectOrderWithDetails(id);

        // 查询订单明细
        List<OrderDetail> details = orderDetailMapper.selectByOrderId(order.getId());
        order.setOrderDetails(details);

        return order;
    }

    @Override
    public PageResult pageQuery(OrdersPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());

        // 分页查询订单，XML Mapper 会根据 DTO 条件拼接 SQL
        List<Orders> orders = orderMapper.pageQuery(dto);
        Page<Orders> page = (Page<Orders>) orders;

        for (Orders o : orders) {
            // 查询订单明细
            List<OrderDetail> details = orderDetailMapper.selectByOrderId(o.getId());
            o.setOrderDetails(details);
        }

        long total = page.getTotal();
        List<Orders> result = page.getResult();

        log.info("分页总数 total = {}", total);
        log.info("查询结果 size = {}", result.size());

        return new PageResult(total, result);
    }




}
