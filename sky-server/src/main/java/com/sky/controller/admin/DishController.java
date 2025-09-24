package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@CrossOrigin
@ApiOperation("菜品相关接口")
public class DishController {
    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result addDish(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}",dishDTO);
        dishService.addDish(dishDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("菜品分页查找")
    public Result<PageResult<DishVO>> pageQuery(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询：{}",dishPageQueryDTO);
        PageResult<DishVO> pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result delete(@RequestParam List<Long> ids){
        // TODO: 传入的字符串，为什么可以直接接收List
        log.info("删除菜品：{}",ids);
        dishService.delete(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询菜品")
    public Result<DishVO> getDish(@PathVariable Long id){
        log.info("根据ID查询菜品：{}",id);
        DishVO dishVO = dishService.getDish(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO){
        log.info("修改菜品：{}",dishDTO);
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售与停售")
    public Result switchStatus(@PathVariable Integer status, Long id){
        log.info("菜品起售与停售：{},{}",id,status);
        dishService.switchStatus(id,status);
        return Result.success();
    }

}
