package com.itheima.controller.user;

import com.itheima.pojo.entity.Category;
import com.itheima.result.Result;
import com.itheima.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/category")
@Tag(name = "用户端分类相关接口")
@Slf4j
public class UserCategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    @Operation(summary = "查询分类")
    public Result<List<Category>> list(Integer type) {
        log.info("用户端查询分类，type={}", type);
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
