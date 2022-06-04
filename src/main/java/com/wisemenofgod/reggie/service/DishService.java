package com.wisemenofgod.reggie.service;
/*
    com.wisemenofgod
    2022-05-30-20:54
*/

import com.baomidou.mybatisplus.extension.service.IService;
import com.wisemenofgod.reggie.dto.DishDto;
import com.wisemenofgod.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dto);

    public DishDto getDishByFlavor(Long id);

    public void updateWithFlavor(DishDto dishdto);

    public void removeWithOther(List<Long> ids);
}
