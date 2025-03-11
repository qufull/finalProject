package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class UserException extends BaseException {
    public UserException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
