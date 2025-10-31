package com.itheima.controller.admin;

import com.itheima.pojo.dto.CategoryDTO;
import com.itheima.pojo.dto.CategoryPageQueryDTO;
import com.itheima.pojo.dto.EmployeePageQueryDTO;
import com.itheima.pojo.vo.CategoryVO;
import com.itheima.result.PageResult;
import com.itheima.result.Result;
import com.itheima.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@Tag(name = "分类相关接口", description = "菜品分类、套餐分类")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @Operation(summary = "新增分类")
    public Result save(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类：{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "分类分页查询")
    public Result<PageResult> page(
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) Integer type,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        CategoryPageQueryDTO categoryPageQueryDTO = new CategoryPageQueryDTO();
        categoryPageQueryDTO.setName(name);
        categoryPageQueryDTO.setType(type);
        categoryPageQueryDTO.setPage(page);
        categoryPageQueryDTO.setPageSize(pageSize);

        log.info("分类分页查询：{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @Operation(summary = "启用禁用分类")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("启用禁用分类：status={}, id={}", status, id);
        categoryService.startOrStop(status, id);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "修改分类")
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类：{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    @DeleteMapping
    @Operation(summary = "根据id删除分类")
    public Result delete(Long id) {
        log.info("根据id删除分类：{}", id);
        categoryService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "根据类型查询分类")
    public Result<List<CategoryVO>> list(Integer type) {
        log.info("根据类型查询分类：{}", type);
        List<CategoryVO> list = categoryService.listByType(type);
        return Result.success(list);
    }

}
