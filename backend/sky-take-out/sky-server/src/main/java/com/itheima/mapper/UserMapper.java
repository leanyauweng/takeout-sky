package com.itheima.mapper;

import com.itheima.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * 根据用户名查询员工
     * @param phone
     * @return
     */
    @Select("select * from user where phone = #{phone}")
    User getByUserPhone(String phone);

    Integer countByMap(Map map);
}
