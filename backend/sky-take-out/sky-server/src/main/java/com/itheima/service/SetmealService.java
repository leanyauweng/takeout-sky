package com.itheima.service;

import com.itheima.pojo.dto.SetmealDTO;
import com.itheima.pojo.dto.SetmealPageQueryDTO;
import com.itheima.pojo.entity.Setmeal;
import com.itheima.pojo.vo.DishItemVO;
import com.itheima.pojo.vo.SetmealVO;
import com.itheima.result.PageResult;

import java.util.List;

public interface SetmealService {
    List<Setmeal> list(Setmeal setmeal);

    List<DishItemVO> getDishItemById(Long id);

    void saveWithDish(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO queryDTO);

    SetmealVO getByIdWithDish(Long id);

    void updateWithDish(SetmealDTO setmealDto);

    void deleteBatch(List<Long> ids);

    void startOrStop(Integer status, Long id);

    List<Setmeal> listByCategoryId(Long categoryId);
}
