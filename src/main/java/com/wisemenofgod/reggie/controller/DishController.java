package com.wisemenofgod.reggie.controller;
/*
    com.wisemenofgod
    2022-05-31-16:04
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisemenofgod.reggie.common.R;
import com.wisemenofgod.reggie.dto.DishDto;
import com.wisemenofgod.reggie.entity.*;
import com.wisemenofgod.reggie.service.CategoryService;
import com.wisemenofgod.reggie.service.DishFlavorService;
import com.wisemenofgod.reggie.service.DishService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CacheManager cacheManager;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishdto){


        //dishService.saveWithFlavor(dishdto);
        dishService.updateWithFlavor(dishdto);

        return R.success("新增菜品成功!");

    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishdto){


        //dishService.saveWithFlavor(dishdto);
        dishService.updateWithFlavor(dishdto);

        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);

        return R.success("新增菜品成功!");

    }

    @GetMapping("/page")
    public R<Page> page(int page , int pageSize , String name){

        Page<Dish> pageInfo = new Page(page,pageSize);

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(name!=null , Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo, queryWrapper);

        Page<DishDto> dtoPage = new Page<>(page, pageSize);
        BeanUtils.copyProperties(pageInfo, dtoPage,"records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dishDtoList = new ArrayList<>();
        for (Dish record : records) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(record, dishDto);
            Category category = categoryService.getById(record.getCategoryId());
            dishDto.setCategoryName(category.getName());
            dishDtoList.add(dishDto);
        }
        dtoPage.setRecords(dishDtoList);
        return R.success(dtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> getDishDto(@PathVariable Long id){

        DishDto dishDto = dishService.getDishByFlavor(id);
        return R.success(dishDto);
    }

//    @GetMapping("/list")
//    public R<List<Dish>> getDishList(Dish dish){
//
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Dish::getStatus, 1);
//        queryWrapper.eq(Dish::getCategoryId, dish.getCategoryId());
//        queryWrapper.orderByAsc(Dish::getSort);
//
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> getDishList(Dish dish){
        List<DishDto> dtoList = null;

        String redisDishKey = "dish_"+dish.getCategoryId()+"__"+dish.getStatus();
        ValueOperations valueOperations = redisTemplate.opsForValue();
        dtoList =(List<DishDto>) valueOperations.get(redisDishKey);
        if (dtoList!=null){
            return R.success(dtoList);
        }


        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.eq(Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.orderByAsc(Dish::getSort);

        List<Dish> list = dishService.list(queryWrapper);


        dtoList = new ArrayList<>();
        for (Dish dish1 : list) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish1, dishDto);

            Long id = dish1.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId, id);
            List<DishFlavor> list1 = dishFlavorService.list(queryWrapper1);

            dishDto.setFlavors(list1);
            dtoList.add(dishDto);
        }
        valueOperations.set(redisDishKey, dtoList,60, TimeUnit.MINUTES);
        return R.success(dtoList);
    }

    @DeleteMapping
    public R<String> del(@RequestParam List<Long> ids){
        dishService.removeWithOther(ids);
        return R.success("删除成功!");
    }

    @PostMapping("/status/{s}")
    public R<String> changeStatus(@PathVariable int s ,@RequestParam List<Long> ids){

//        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
//        queryWrapper.in(Setmeal::getId, ids);
//        queryWrapper.eq(Setmeal::getStatus, s);
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Dish::getId, ids);
        updateWrapper.set(Dish::getStatus, s);

        boolean update = dishService.update(updateWrapper);
        if (update){
            return R.success("操作成功!");
        }

        return R.error("操作失败");
    }


}
