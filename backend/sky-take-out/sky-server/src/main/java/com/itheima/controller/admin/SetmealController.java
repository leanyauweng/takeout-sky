package com.itheima.controller.admin;

import com.itheima.pojo.dto.DishPageQueryDTO;
import com.itheima.pojo.dto.SetmealDTO;
import com.itheima.pojo.dto.SetmealPageQueryDTO;
import com.itheima.pojo.entity.Setmeal;
import com.itheima.pojo.vo.SetmealVO;
import com.itheima.result.PageResult;
import com.itheima.result.Result;
import com.itheima.service.SetmealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
@Tag(name = "套餐相关接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    // 新增套餐
    @PostMapping
    @Operation(summary = "新增套餐")
    @CacheEvict(cacheNames = "setmealCache",key = "#setmealDTO.categoryId")
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    // 分页查询套餐
    @GetMapping("/page")
    @Operation(summary = "套餐分页查询")
    public Result<PageResult> pageQuery(
            @RequestParam Integer page,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status) {
        SetmealPageQueryDTO queryDTO = new SetmealPageQueryDTO();
        queryDTO.setCategoryId(categoryId);
        queryDTO.setName(name);
        queryDTO.setStatus(status);
        queryDTO.setPage(page);
        queryDTO.setPageSize(pageSize);
        log.info("分页查询：{}", queryDTO);
        PageResult pageResult = setmealService.pageQuery(queryDTO);
        return Result.success(pageResult);
    }

    // 根据id查询套餐（回显）
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }

    // 修改套餐
    @PutMapping
    @Operation(summary = "修改套餐")
    @CacheEvict(cacheNames = "setmealCache",key = "#setmealDTO.categoryId")
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        setmealService.updateWithDish(setmealDTO);
        return Result.success();
    }

    // 删除套餐
    @DeleteMapping
    @Operation(summary = "批量删除套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result delete(@RequestParam List<Long> ids) {
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    // 修改套餐状态
    @PostMapping("/status/{status}")
    @Operation(summary = "套餐起售停售")
    public Result startOrStop(@PathVariable Integer status,
                              @RequestParam Long id) {  // 必须加 @RequestParam
        if (id == null) {
            return Result.error("套餐ID不能为空");
        }
        setmealService.startOrStop(status, id);
        return Result.success();
    }

    // 根据分类ID查询套餐列表
    @GetMapping("/list")
    @Operation(summary = "根据分类ID查询套餐列表")
    public Result<List<Setmeal>> list(@RequestParam Long categoryId) {
        log.info("根据分类ID查询套餐列表：{}", categoryId);
        List<Setmeal> list = setmealService.listByCategoryId(categoryId);
        return Result.success(list);
    }

}

