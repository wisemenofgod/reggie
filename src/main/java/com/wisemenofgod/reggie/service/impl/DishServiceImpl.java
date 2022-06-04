package com.wisemenofgod.reggie.service.impl;
/*
    com.wisemenofgod
    2022-05-30-20:55
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisemenofgod.reggie.dto.DishDto;
import com.wisemenofgod.reggie.entity.Dish;
import com.wisemenofgod.reggie.entity.DishFlavor;
import com.wisemenofgod.reggie.mapper.DishMapper;
import com.wisemenofgod.reggie.service.DishFlavorService;
import com.wisemenofgod.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SuppressWarnings("all")
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;


    @Transactional
    public void saveWithFlavor(DishDto dto) {
        //保存菜品相关信息
        save(dto);

        Long id = dto.getId();
        for (DishFlavor flavor : dto.getFlavors()) {
            flavor.setDishId(id);
        }

        //保存菜品口味相关信息
        dishFlavorService.saveBatch(dto.getFlavors());
    }

    @Override
    public DishDto getDishByFlavor(Long id) {

        Dish dish = getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);

        return dishDto;
    }

    @Transactional
    public void updateWithFlavor(DishDto dishdto) {
        updateById(dishdto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, dishdto.getId());
        dishFlavorService.remove(queryWrapper);

        Long id = dishdto.getId();
        for (DishFlavor flavor : dishdto.getFlavors()) {
            flavor.setDishId(id);
        }

        //保存菜品口味相关信息
        dishFlavorService.saveBatch(dishdto.getFlavors());
    }

    @Override
    public void removeWithOther(List<Long> ids) {


    }
}
