package com.wisemenofgod.reggie.dto;

import com.wisemenofgod.reggie.entity.Dish;

import com.wisemenofgod.reggie.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
