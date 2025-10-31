package com.itheima.mapper;

import com.itheima.annotation.AutoFill;
import com.itheima.enumeration.OperationType;
import com.itheima.pojo.dto.CategoryPageQueryDTO;
import com.itheima.pojo.entity.Category;
import com.itheima.pojo.vo.CategoryVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 新增分类
     */
    @Insert("INSERT INTO category " +
            "(name, type, sort, status, create_time, update_time, create_user, update_user) " +
            "VALUES " +
            "(#{name}, #{type}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @AutoFill(value = OperationType.INSERT)
    void insert(Category category);

    /**
     * 根据id删除分类
     */
    void deleteById(Long id);

    /**
     * 修改分类（动态 SQL 在 xml 中）
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    /**
     * 分页查询分类（动态 SQL 在 xml 中）
     */
    List<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据类型查询分类列表
     */
    List<CategoryVO> listByType(Integer type);

    List<Category> list(Integer type);
}
