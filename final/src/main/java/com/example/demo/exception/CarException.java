package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class CarException extends BaseException{
    public CarException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
