package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.exception.DeletionNotAllowedException;
import com.itheima.exception.SetmealOperationException;
import com.itheima.mapper.SetmealDishMapper;
import com.itheima.mapper.SetmealMapper;
import com.itheima.pojo.dto.SetmealDTO;
import com.itheima.pojo.dto.SetmealPageQueryDTO;
import com.itheima.pojo.entity.Setmeal;
import com.itheima.pojo.entity.SetmealDish;
import com.itheima.pojo.vo.DishItemVO;
import com.itheima.pojo.vo.SetmealVO;
import com.itheima.result.PageResult;
import com.itheima.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }

    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        // 1. 保存套餐基本信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.setStatus(1);
        setmealMapper.insert(setmeal);

        // 2. 保存套餐和菜品的关系
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            for (SetmealDish sd : setmealDishes) {
                sd.setSetmealId(setmealId);
            }
            setmealDishMapper.insertBatch(setmealDishes);
        }

    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(queryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public SetmealVO getByIdWithDish(Long id) {
        // 查套餐基本信息
        SetmealVO setmealVO = setmealMapper.getById(id);
        // 查套餐关联的菜品
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    @Override
    public void updateWithDish(SetmealDTO setmealDTO) {
        // 1. 更新套餐基本信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        // 2. 先删旧的套餐菜品关系，再新增
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            for (SetmealDish sd : setmealDishes) {
                sd.setSetmealId(setmealDTO.getId());
            }
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        // 1. 判断套餐是否起售中
        for (Long id : ids) {
            Integer status = setmealMapper.getStatusById(id);
            if (status != null && status == 1) { // 1 表示起售中
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        // 删除套餐
        setmealMapper.deleteBatch(ids);
        // 删除关联的菜品
        setmealDishMapper.deleteBySetmealIds(ids);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        if (status == 1) { // 起售
            // 调用更新后的 Mapper 方法
            List<SetmealDish> dishes = setmealDishMapper.getBySetmealIdWithStatus(id);
            for (SetmealDish dish : dishes) {
                if (dish.getStatus() == 0) { // 菜品未启售
                    throw new SetmealOperationException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
        }
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setStatus(status);
        setmealMapper.updateStatus(setmeal);
    }

    @Override
    public List<Setmeal> listByCategoryId(Long categoryId) {
        return setmealMapper.listByCategoryId(categoryId);
    }
}
