package com.wisemenofgod.reggie.service;
/*
    com.wisemenofgod
    2022-05-30-19:03
*/

import com.baomidou.mybatisplus.extension.service.IService;
import com.wisemenofgod.reggie.entity.Category;
@SuppressWarnings("all")
public interface CategoryService extends IService<Category> {

    public void remove(Long id);
}
