package com.wisemenofgod.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisemenofgod.reggie.entity.OrderDetail;
import com.wisemenofgod.reggie.mapper.OrderDetailMapper;
import com.wisemenofgod.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}