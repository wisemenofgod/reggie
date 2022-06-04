package com.wisemenofgod.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisemenofgod.reggie.entity.ShoppingCart;
import com.wisemenofgod.reggie.mapper.ShoppingCartMapper;
import com.wisemenofgod.reggie.service.ShoppingCartService;
import com.wisemenofgod.reggie.entity.ShoppingCart;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
