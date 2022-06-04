package com.wisemenofgod.reggie.mapper;
/*
    com.wisemenofgod
    2022-05-30-20:53
*/

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisemenofgod.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@SuppressWarnings("all")
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
