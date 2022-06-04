package com.wisemenofgod.reggie.service.impl;
/*
    com.wisemenofgod
    2022-05-30-20:56
*/

import com.alibaba.fastjson.util.ServiceLoader;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisemenofgod.reggie.common.CustomException;
import com.wisemenofgod.reggie.dto.SetmealDto;
import com.wisemenofgod.reggie.entity.Category;
import com.wisemenofgod.reggie.entity.Setmeal;
import com.wisemenofgod.reggie.entity.SetmealDish;
import com.wisemenofgod.reggie.mapper.SetmealMapper;
import com.wisemenofgod.reggie.service.CategoryService;
import com.wisemenofgod.reggie.service.SetmealDishService;
import com.wisemenofgod.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper , Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    @Transactional
    @Override
    public void saveWithSetmealDish(SetmealDto setmealDto) {
        save(setmealDto);

        Long id = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(id);
        }
        setmealDishService.saveBatch(setmealDishes);

    }

    @Override
    @Transactional
    public void removeWithSetmealDish(List<Long> ids) {
        //若是在售卖的套餐,则不能删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);

        int count = count(queryWrapper);
        if (count>0){
            throw new CustomException("套餐正在售卖中,不能删除!");
        }
        //删除setmeal
        removeByIds(ids);

        //删除setmealdish
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(queryWrapper1);
    }

    @Override
    @Transactional
    public SetmealDto getSetmealWithOther(Long id) {
//        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
//        queryWrapper.eq(Setmeal::getId, id);
//        getById()
        Setmeal setmeal = getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        Category category = categoryService.getById(setmeal.getCategoryId());
        setmealDto.setCategoryName(category.getName());

        List<SetmealDish> list = new ArrayList<>();

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> setmealDishList = setmealDishService.list(queryWrapper);

        setmealDto.setSetmealDishes(setmealDishList);


        return setmealDto;
    }

    @Override
    @Transactional
    public void updateWithOther(SetmealDto dto) {
        updateById(dto);

        Long id = dto.getId();
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        setmealDishService.remove(queryWrapper);

        List<SetmealDish> setmealDishes = dto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(id);
        }

        setmealDishService.saveBatch(setmealDishes);
    }
}
