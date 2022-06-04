package com.wisemenofgod.reggie.mapper;
/*
    com.wisemenofgod
    2022-06-02-16:52
*/

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisemenofgod.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
