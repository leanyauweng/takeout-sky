package com.itheima.controller.user;

import com.itheima.constant.StatusConstant;
import com.itheima.pojo.entity.Setmeal;
import com.itheima.pojo.vo.DishItemVO;
import com.itheima.result.Result;
import com.itheima.service.SetmealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/setmeal")
@Tag(name = "用户端套餐相关接口")
@Slf4j
public class UserSetmealController {
    @Autowired
    private SetmealService setmealService;

    @GetMapping("/list")
    @Operation(summary = "根据分类id查询套餐")
    @Cacheable(cacheNames =  "setmealCache",key = "#categoryId")
    public Result<List<Setmeal>> list(Long categoryId) {
        log.info("用户端根据分类id查询套餐，categoryId={}", categoryId);

        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);

        List<Setmeal> list = setmealService.list(setmeal);
        return Result.success(list);
    }

    @GetMapping("/dish/{id}")
    @Operation(summary = "根据套餐id查询菜品")
    public Result<List<DishItemVO>> getDishItemBySetmealId(@PathVariable Long id) {
        log.info("查询套餐下的菜品，套餐id={}", id);
        List<DishItemVO> list = setmealService.getDishItemById(id);
        return Result.success(list);
    }
}
