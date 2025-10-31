package com.itheima.service;

import com.itheima.pojo.dto.CategoryDTO;
import com.itheima.pojo.dto.CategoryPageQueryDTO;
import com.itheima.pojo.entity.Category;
import com.itheima.pojo.vo.CategoryVO;
import com.itheima.result.PageResult;

import java.util.List;

public interface CategoryService {
    /**
     * 新增分类
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 分页查询分类
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 启用禁用分类
     */
    void startOrStop(Integer status, Long id);

    /**
     * 修改分类
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 根据id删除分类
     */
    void deleteById(Long id);

    /**
     * 根据类型查询分类
     */
    List<CategoryVO> listByType(Integer type);

    List<Category> list(Integer type);
}
