package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 添加菜品
     * @param dishDTO
     */
    void addDish(DishDTO dishDTO);

    PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);
    /**
     * 根据id删除菜品
     * @param ids
     * @return
     */
    void delete(List<Long> ids);

    /**
     * 根据菜品ID获取菜品信息
     * @param id
     * @return
     */
    DishVO getDish(Long id);

    /**
     * 更新菜品信息
     * @param dishDTO
     */
    void updateDish(DishDTO dishDTO);

    /**
     * 菜品起售与停售
     * @param id
     * @param status
     */
    void switchStatus(Long id, Integer status);
}
