package com.wisemenofgod.reggie.service.impl;
/*
    com.wisemenofgod
    2022-06-02-16:53
*/

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisemenofgod.reggie.entity.AddressBook;
import com.wisemenofgod.reggie.mapper.AddressBookMapper;
import com.wisemenofgod.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

@SuppressWarnings("all")
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper , AddressBook> implements AddressBookService{
}
