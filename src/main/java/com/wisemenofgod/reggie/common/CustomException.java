package com.wisemenofgod.reggie.common;
/*
    com.wisemenofgod
    2022-05-30-21:11
*/

@SuppressWarnings("all")
public class CustomException extends RuntimeException{

    public CustomException(String message) {
        super(message);
    }

    public CustomException() {
    }
}
