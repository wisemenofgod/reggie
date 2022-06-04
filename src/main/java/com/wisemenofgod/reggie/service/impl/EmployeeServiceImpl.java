package com.wisemenofgod.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisemenofgod.reggie.entity.Employee;
import com.wisemenofgod.reggie.mapper.EmployeeMapper;
import com.wisemenofgod.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
