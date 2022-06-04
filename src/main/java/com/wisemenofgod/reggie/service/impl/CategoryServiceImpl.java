package com.wisemenofgod.reggie.service.impl;
/*
    com.wisemenofgod
    2022-05-30-19:04
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisemenofgod.reggie.common.CustomException;
import com.wisemenofgod.reggie.entity.Category;
import com.wisemenofgod.reggie.entity.Dish;
import com.wisemenofgod.reggie.entity.Setmeal;
import com.wisemenofgod.reggie.mapper.CategoryMapper;
import com.wisemenofgod.reggie.service.CategoryService;
import com.wisemenofgod.reggie.service.DishService;
import com.wisemenofgod.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("all")
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper , Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {

        //查询dish里面是否有 这个类型的菜品
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(queryWrapper);
        if (count > 0 ){
            //抛异常
            throw new CustomException("当前的分类下关联了菜品,无法删除!!!");
        }

        LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Setmeal::getCategoryId,id);
        int count1 = setmealService.count(queryWrapper1);
        if (count1 > 0){
            throw new CustomException("当前的分类下关联了套餐,无法删除!!!");
        }

        removeById(id);

    }
}
