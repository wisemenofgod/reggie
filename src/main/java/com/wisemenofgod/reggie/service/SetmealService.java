package com.wisemenofgod.reggie.service;
/*
    com.wisemenofgod
    2022-05-30-20:54
*/

import com.baomidou.mybatisplus.extension.service.IService;
import com.wisemenofgod.reggie.dto.SetmealDto;
import com.wisemenofgod.reggie.entity.Setmeal;
import com.wisemenofgod.reggie.entity.SetmealDish;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {



    public void saveWithSetmealDish(SetmealDto setmealDto);

    public void removeWithSetmealDish(List<Long> ids);
    public SetmealDto getSetmealWithOther(Long id);

    public void updateWithOther(SetmealDto dto);
}
