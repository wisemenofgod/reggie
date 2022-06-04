package com.wisemenofgod.reggie.mapper;
/*
    com.wisemenofgod
    2022-05-30-19:02
*/

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisemenofgod.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@SuppressWarnings("all")
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
