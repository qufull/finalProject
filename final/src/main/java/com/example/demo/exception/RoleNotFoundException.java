package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends BaseException{
    public RoleNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
