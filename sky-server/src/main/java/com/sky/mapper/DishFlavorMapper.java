package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {


    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品id删除口味
     *
     * @param dishId
     * @return
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);


    /**
     * 批量删除菜品相关口味数据
     * @param ids
     */

    void deleteByDishIds(List<Long> ids);

    /**
     * 根据菜品ID 查询口味信息
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> getFlavors(Long id);
}
