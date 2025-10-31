package com.itheima.mapper;

import com.itheima.annotation.AutoFill;
import com.itheima.enumeration.OperationType;
import com.itheima.pojo.dto.GoodSalesDTO;
import com.itheima.pojo.dto.OrdersPageQueryDTO;
import com.itheima.pojo.entity.Orders;
import com.itheima.pojo.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.core.annotation.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    void insert(Orders orders);

    @Select("SELECT * FROM orders WHERE status = #{status} AND order_time < #{time}")
    List<Orders> getByStatusAndOrderTimeLT(@Param("status") Integer status, @Param("time") LocalDateTime time);

    void update(Orders orders);

    @Select("SELECT * FROM orders WHERE id = #{id}")
    Orders getById(Long id);

    Double sumByMap(Map map);

    Integer countByMap(Map map);

    List<GoodSalesDTO> getSalesTop10(LocalDateTime begin,LocalDateTime end);

    int cancelById(@Param("id") Long id,
                    @Param("status") Integer status,
                    @Param("reason") String reason);


    int rejectById(@Param("id") Long id,
                   @Param("status") Integer status,
                   @Param("reason") String reason);

    Orders selectById(Long id);

    Orders selectOrderWithDetails(Long id);

    List<Orders> pageQuery(OrdersPageQueryDTO dto);

    int admincancelById(@Param("id") Long id,
                        @Param("status") Integer status,
                        @Param("reason") String reason);

    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    @Select("""
        SELECT *
        FROM orders o
        WHERE o.user_id = #{userId}
        ORDER BY o.create_time DESC
    """)
    List<OrderVO> findByUserId(Long userId);
}
