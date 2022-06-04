package com.wisemenofgod.reggie.common;
/*
    com.wisemenofgod
    2022-05-30-18:07
*/

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wisemenofgod.reggie.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@SuppressWarnings("all")
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Autowired
    public HttpServletRequest request;
    @Override
    public void insertFill(MetaObject metaObject) {
        log.error("insert");
        log.info(metaObject.getOriginalObject().toString());
//        Employee employee = (Employee) metaObject.getOriginalObject();
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser((Long) request.getSession().getAttribute("employee"));
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());

        log.info(metaObject.getOriginalObject().toString());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.error("update");
        log.info(metaObject.getOriginalObject().toString());
//        Employee employee = (Employee) metaObject.getOriginalObject();
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
        log.info(metaObject.getOriginalObject().toString());

    }
}
