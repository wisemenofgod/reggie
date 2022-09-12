package com.wisemenofgod.reggie.controller;
/*
    com.wisemenofgod
    2022-06-02-10:18
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisemenofgod.reggie.common.R;
import com.wisemenofgod.reggie.dto.DishDto;
import com.wisemenofgod.reggie.dto.SetmealDto;
import com.wisemenofgod.reggie.entity.Category;
import com.wisemenofgod.reggie.entity.Dish;
import com.wisemenofgod.reggie.entity.Setmeal;
import com.wisemenofgod.reggie.service.CategoryService;
import com.wisemenofgod.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;


    //新增套餐 + 并添加setmeal_dish
    @PostMapping
    @CacheEvict(value = "setmealCache" , allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto){

        setmealService.saveWithSetmealDish(setmealDto);

        return R.success("保存套餐成功!");
    }

    //分页查询套餐
    @GetMapping("/page")
    public R<Page> page(int page , int pageSize , String name) {

        Page<Setmeal> pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(name!=null,Setmeal::getName, name);
        queryWrapper.orderByAsc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        Page<SetmealDto> pageDto = new Page(page, pageSize);
        BeanUtils.copyProperties(pageInfo, pageDto,"records");

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = new ArrayList<>();
        for (Setmeal record : records) {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(record, setmealDto);
            Category category = categoryService.getById(record.getCategoryId());

            setmealDto.setCategoryName(category.getName());
            list.add(setmealDto);
        }
        pageDto.setRecords(list);


        return R.success(pageDto);
    }

    //删除套餐 + 并判断是否停售
    @DeleteMapping
    @CacheEvict(value = "setmealCache" , allEntries = true)
    public R<String> del(@RequestParam List<Long> ids){
        setmealService.removeWithSetmealDish(ids);
        return R.success("删除成功!");
    }


    //套餐进行停售 + 批量停售
    @PostMapping("/status/{s}")
    @CacheEvict(value = "setmealCache" , allEntries = true)
    public R<String> changeStatus(@PathVariable int s ,@RequestParam List<Long> ids){

//        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
//        queryWrapper.in(Setmeal::getId, ids);
//        queryWrapper.eq(Setmeal::getStatus, s);
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Setmeal::getId, ids);
        updateWrapper.set(Setmeal::getStatus, s);

        boolean update = setmealService.update(updateWrapper);
        if (update){
            return R.success("操作成功!");
        }

        return R.error("操作失败");
    }


    //修改套餐是 进行回表查询 + 数据展现到页面上
    @GetMapping("/{id}")
    public R<SetmealDto> getSetmealDto(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getSetmealWithOther(id);
        return R.success(setmealDto);
    }

    //更新套餐的信息  先更新 setmeal表,在根据id删除setmeal_dish表中的数据,在重新批量添加
    @PutMapping
    @CacheEvict(value = "setmealCache" , allEntries = true)
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithOther(setmealDto);

        return R.success("保存套餐成功!");
    }


    //根据套餐的分类id查询出该分类下的所有套餐
    @GetMapping("/list")
    @Cacheable(value = "setmealCache" , key = "#setmeal.categoryId+'_'+#setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Setmeal::getCategoryId, setmeal.getCategoryId()).eq(Setmeal::getStatus, setmeal.getStatus());
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

}
