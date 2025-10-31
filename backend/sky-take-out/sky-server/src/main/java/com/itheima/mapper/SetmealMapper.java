package com.itheima.mapper;

import com.github.pagehelper.Page;
import com.itheima.pojo.dto.SetmealPageQueryDTO;
import com.itheima.pojo.entity.Setmeal;
import com.itheima.pojo.vo.DishItemVO;
import com.itheima.pojo.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {
    /**
     * 根据分类id统计关联套餐数量
     */
    @Select("SELECT COUNT(*) FROM setmeal WHERE category_id = #{categoryId}")
    int countByCategoryId(Long categoryId);


    List<Setmeal> list(Setmeal setmeal);

    List<DishItemVO> getDishItemBySetmealId(Long id);

    void insert(Setmeal setmeal);

    Page<SetmealVO> pageQuery(SetmealPageQueryDTO queryDTO);

    SetmealVO getById(Long id);

    void updateStatus(Setmeal setmeal);

    void deleteBatch(List<Long> ids);

    void update(Setmeal setmeal);

    @Select("SELECT status FROM setmeal WHERE id = #{id}")
    Integer getStatusById(Long id);

    Integer countByMap(Map<String, Object> map);

    @Select("SELECT * FROM setmeal WHERE category_id = #{categoryId} AND status = 1 ORDER BY update_time DESC")
    List<Setmeal> listByCategoryId(Long categoryId);
}
