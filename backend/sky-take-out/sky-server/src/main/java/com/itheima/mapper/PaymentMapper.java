package com.itheima.mapper;

import com.itheima.annotation.AutoFill;
import com.itheima.enumeration.OperationType;
import com.itheima.pojo.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaymentMapper {

    Orders getByNumberAndUserId(@Param("number") String orderNumber, @Param("userId") Long userId);

    void update(Orders ordersDB);
}
