package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增菜品Service实现
     * @param dishDTO
     */
    @Override
    @Transactional
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.add(dish);

        // 口味表也得新增（非必须）
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size()>0){
            flavors.forEach(flavor -> {
                flavor.setDishId(dish.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }

    }

    /**
     *菜品分页查询Service实现
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> pageResult = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult<>(pageResult.getTotal(),pageResult.getResult());
    }

    /**
     * 分情况删除删除菜品
     * @param ids
     */
    @Override
    @Transactional
    public void delete(List<Long> ids) {
        // 判断菜品是否能够删除 是否启用
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                // 抛出异常
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 是否被套餐引用
        List<Long> setmealIds = setmealDishMapper.getSetmealIdByDishIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            // 抛出异常
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //批量删除菜品数据
//        for (Long id : ids) {
//             dishMapper.deleteById(id);
//             // 删除菜品口味
//             dishFlavorMapper.deleteByDishId(id);
//         }
        dishMapper.deleteByIds(ids);

        //批量删除菜品关联的口味数据
        dishFlavorMapper.deleteByDishIds(ids);

    }

    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    public DishVO getDish(Long id) {
        // 查询菜品信息
        DishVO dishVO = new DishVO();
        Dish dish = new Dish();
        dish = dishMapper.getDish(id);
        // 查询ID口味信息
        if(dish != null){
            List<DishFlavor> flavors = dishFlavorMapper.getFlavors(id);
            BeanUtils.copyProperties(dish,dishVO);
            dishVO.setFlavors(flavors);
            return dishVO;
        }else{
            return null;
        }
    }

    /**
     * 修改菜品信息
     * @param dishDTO
     */
    @Override
    public void updateDish(DishDTO dishDTO) {
        // 更新菜品信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.updateDish(dish);
        // 更新口味信息
            //删除原有口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!= null && flavors.size() > 0){
            dishFlavorMapper.deleteByDishId(dish.getId());
            dishDTO.getFlavors().forEach(flavor ->{
                flavor.setDishId(dish.getId());
            });
            dishFlavorMapper.insertBatch(dishDTO.getFlavors());
        }
    }

    /**
     * 菜品起售与停售
     * @param id
     * @param status
     */
    @Override
    public void switchStatus(Long id, Integer status) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(id);
        dishMapper.updateDish(dish);

        // 如果为警用，需要将套餐中的也禁用
        // 如果是禁用，需要将套餐中的菜品也禁用
        if (status == StatusConstant.DISABLE) {
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            List<Long> setmealIds = setmealDishMapper.getSetmealIdByDishIds(dishIds);
            if (setmealIds != null && setmealIds.size() > 0) {
                for (Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    setmealMapper.update(setmeal);
                }
            }
        }
    }

    /**
     * 根据菜品分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getFlavors(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

}
