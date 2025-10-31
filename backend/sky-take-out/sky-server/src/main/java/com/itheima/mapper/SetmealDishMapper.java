package com.itheima.mapper;

import com.itheima.pojo.entity.SetmealDish;

import java.util.List;

public interface SetmealDishMapper {

    List<Long> getSetMealIdsByDishIds(List<Long> dishIds);

    void insertBatch(List<SetmealDish> setmealDishes);

    List<SetmealDish> getBySetmealId(Long id);

    void deleteBySetmealId(Long id);

    void deleteBySetmealIds(List<Long> ids);

    List<SetmealDish> getBySetmealIdWithStatus(Long id);
}
