package com.itheima.mapper;

import com.itheima.pojo.entity.OrderDetail;
import com.itheima.pojo.vo.OrderItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface OrderDetailMapper {

    void insertBatch(ArrayList<OrderDetail> orderDetailList);

    @Select("SELECT * FROM order_detail WHERE order_id = #{orderId}")
    List<OrderDetail> selectByOrderId(@Param("orderId") Long orderId);

    @Select("SELECT id, order_id AS orderId, dish_id AS dishId, setmeal_id AS setmealId, " +
            "name, image, dish_flavor AS dishFlavor, number, amount " +
            "FROM order_detail WHERE order_id = #{orderId}")
    List<OrderItemVO> findByOrderId(Long id);
}
