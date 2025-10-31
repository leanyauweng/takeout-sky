package com.itheima.controller.admin;

import com.itheima.pojo.dto.DishDTO;
import com.itheima.pojo.dto.DishPageQueryDTO;
import com.itheima.pojo.vo.DishTop10VO;
import com.itheima.pojo.vo.DishVO;
import com.itheima.result.PageResult;
import com.itheima.result.Result;
import com.itheima.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/dish")
@Tag(name = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    // 新增菜品（带口味）
    @PostMapping
    @Operation(summary = "新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    // 分页查询菜品
    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public Result<PageResult> page(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        DishPageQueryDTO queryDTO = new DishPageQueryDTO();
        queryDTO.setCategoryId(categoryId);
        queryDTO.setName(name);
        queryDTO.setStatus(status);
        queryDTO.setPage(page);
        queryDTO.setPageSize(pageSize);
        log.info("分页查询：{}", queryDTO);
        PageResult pageResult = dishService.pageQuery(queryDTO);
        return Result.success(pageResult);
    }

    @PutMapping
    @Operation(summary = "修改菜品及口味")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品：{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    @DeleteMapping
    @Operation(summary = "删除菜品及口味（支持批量）")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("删除菜品 ids={}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据id查询")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据id查询菜品：{}",id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    // 修改菜品状态
    @PostMapping("/status/{status}")
    @Operation(summary = "菜品起售停售")
    public Result startOrStop(@PathVariable Integer status, @RequestParam Long id) {
        dishService.startOrStop(status, id);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "根据分类ID查询菜品列表")
    public Result<List<DishVO>> list(@RequestParam Long categoryId) {
        log.info("根据分类ID查询菜品列表：{}", categoryId);
        List<DishVO> list = dishService.listByCategoryId(categoryId);
        return Result.success(list);
    }

    @GetMapping("/top10")
    @Operation(summary = "首页展示 Top10 菜品")
    public Result<List<DishTop10VO>> getTop10Dishes() {
        List<DishTop10VO> top10 = dishService.getTop10Dishes();
        return Result.success(top10);
    }

}
