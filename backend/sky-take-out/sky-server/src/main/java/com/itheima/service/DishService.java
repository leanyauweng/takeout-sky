package com.itheima.service;

import com.itheima.pojo.dto.DishDTO;
import com.itheima.pojo.dto.DishPageQueryDTO;
import com.itheima.pojo.entity.Dish;
import com.itheima.pojo.vo.DishTop10VO;
import com.itheima.pojo.vo.DishVO;
import com.itheima.result.PageResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DishService {
    void saveWithFlavor(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO dto);

    void updateWithFlavor(DishDTO dishDTO);

    void deleteWithFlavor(List<Long> ids);

    @Transactional
    void deleteBatch(List<Long> ids);

    DishVO getByIdWithFlavor(Long id);

    List<DishVO> listWithFlavor(Dish dish);

    void startOrStop(Integer status, Long id);

    List<DishVO> listByCategoryId(Long categoryId);

    List<DishTop10VO> getTop10Dishes();
}
