package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class UserCreateException extends BaseException {
    public UserCreateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
