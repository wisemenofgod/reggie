package com.wisemenofgod.reggie.controller;
/*
    com.wisemenofgod
    2022-05-30-19:05
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisemenofgod.reggie.common.R;
import com.wisemenofgod.reggie.entity.Category;
import com.wisemenofgod.reggie.entity.Employee;
import com.wisemenofgod.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("all")
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category){

        log.warn(category.toString());

        boolean save = categoryService.save(category);
        if (save){
            return R.success("增加菜品分类成功");
        }
        return R.error("增加菜品分类失败");
    }

    @GetMapping("/page")
    public R<Page> page(int page , int pageSize , String name){
        log.info("{}---------------{}",page , pageSize);
        Page pageInfo = new Page(page,pageSize);

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getType).orderByAsc(Category::getSort);

        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> del(Long ids){

        categoryService.remove(ids);
        return R.success("已成功删除!");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){

        boolean b = categoryService.updateById(category);
        if (b){
            return R.success("修改成功");
        }
        return R.error("未修改,修改失败!");


    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category){

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(category.getType()!=null,Category::getType, category.getType());
//        queryWrapper.orderByAsc(Category::getSort);
        queryWrapper.orderByAsc(Category::getType).orderByAsc(Category::getSort);
        List<Category> list = categoryService.list(queryWrapper);

        return R.success(list);
    }
}
