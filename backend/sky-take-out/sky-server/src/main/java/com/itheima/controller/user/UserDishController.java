package com.itheima.controller.user;

import com.itheima.constant.StatusConstant;
import com.itheima.pojo.entity.Dish;
import com.itheima.pojo.vo.DishVO;
import com.itheima.result.Result;
import com.itheima.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/dish")
@Tag(name = "用户端菜品相关接口")
@Slf4j
public class UserDishController {

    @Autowired
    private DishService dishService;

    @GetMapping("/list")
    @Operation(summary = "根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        log.info("用户端根据分类id查询菜品，categoryId={}", categoryId);
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);
        List<DishVO> list = dishService.listWithFlavor(dish);
        return Result.success(list);
    }
}
