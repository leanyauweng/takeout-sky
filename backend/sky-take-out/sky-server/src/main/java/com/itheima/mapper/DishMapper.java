package com.itheima.mapper;

import com.itheima.annotation.AutoFill;
import com.itheima.enumeration.OperationType;
import com.itheima.pojo.dto.DishDTO;
import com.itheima.pojo.dto.DishPageQueryDTO;
import com.itheima.pojo.entity.Dish;
import com.itheima.pojo.vo.DishTop10VO;
import com.itheima.pojo.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {
    /**
     * 根据分类id统计关联菜品数量
     */
    @Select("SELECT COUNT(*) FROM dish WHERE category_id = #{categoryId}")
    int countByCategoryId(Long categoryId);

    List<DishVO> pageQuery(DishPageQueryDTO dto);

    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    void update(Dish dish);

    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    void deleteByIds(List<Long> ids);

    List<Dish> list(Dish dish);

    void updateStatus(Dish dish);

    Integer countByMap(Map<String, Object> map);

    List<DishVO> listByCategoryId(Long categoryId);

    List<DishTop10VO> selectTop10Dishes();
}
