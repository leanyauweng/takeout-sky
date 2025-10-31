package com.itheima.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.StatusConstant;
import com.itheima.exception.DeletionNotAllowedException;
import com.itheima.mapper.DishFlavorMapper;
import com.itheima.mapper.DishMapper;
import com.itheima.mapper.SetmealDishMapper;
import com.itheima.mapper.SetmealMapper;
import com.itheima.pojo.dto.DishDTO;
import com.itheima.pojo.dto.DishPageQueryDTO;
import com.itheima.pojo.entity.Dish;
import com.itheima.pojo.entity.DishFlavor;
import com.itheima.pojo.entity.Setmeal;
import com.itheima.pojo.vo.DishTop10VO;
import com.itheima.pojo.vo.DishVO;
import com.itheima.result.PageResult;
import com.itheima.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class DishServiceImpl implements DishService {

    @Autowired private DishMapper dishMapper;

    @Autowired private DishFlavorMapper dishFlavorMapper;

    @Autowired private SetmealMapper setmealMapper;

    @Autowired private SetmealDishMapper setmealDishMapper;

    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        log.info("开始保存菜品: {}", dishDTO);
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dish.setStatus(1);
        // 保存 dish
        dishMapper.insert(dish);

        Long dishId = dish.getId();
        // 保存 flavor
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishflavor -> dishflavor.setDishId(dishId));
            log.info("口味列表: {}", dishDTO.getFlavors());
            dishFlavorMapper.insertBatch(flavors);
        }

    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        List<DishVO> list = dishMapper.pageQuery(dto);
        PageInfo<DishVO> pageInfo = new PageInfo<>(list);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());

    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        // 更新菜品
        dishMapper.update(dish);

        Long dishId = dishDTO.getId();

        // 先删除原有口味
        dishFlavorMapper.deleteByDishId(dishId);

        // 再插入口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(f -> f.setDishId(dishId));
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public void deleteWithFlavor(List<Long> ids) {

    }

    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        //判断菜品是否起售中
        for(Long id : ids){
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                //起售中无法删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断菜品是否关联套餐
        List<Long> setmealIds = setmealDishMapper.getSetMealIdsByDishIds(ids);
        if(setmealIds !=null && setmealIds.size() > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 批量删除（一次性）
        dishFlavorMapper.deleteByDishIds(ids);
        dishMapper.deleteByIds(ids);

    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        //根据id查询菜品数据
        Dish dish = dishMapper.getById(id);

        //根据菜品id查询口味数据
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);

        //将查询到的数据封装到VO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }
        return dishVOList;
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishMapper.updateStatus(dish);
    }

    @Override
    public List<DishVO> listByCategoryId(Long categoryId) {
        return dishMapper.listByCategoryId(categoryId);
    }

    @Override
    public List<DishTop10VO> getTop10Dishes() {
        return dishMapper.selectTop10Dishes();
    }


}
