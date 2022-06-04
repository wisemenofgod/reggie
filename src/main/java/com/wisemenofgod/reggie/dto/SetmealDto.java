package com.wisemenofgod.reggie.dto;

import com.wisemenofgod.reggie.entity.Setmeal;
import com.wisemenofgod.reggie.entity.SetmealDish;
import com.wisemenofgod.reggie.entity.Setmeal;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
