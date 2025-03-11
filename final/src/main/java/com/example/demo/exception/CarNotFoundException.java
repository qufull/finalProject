package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class CarNotFoundException extends BaseException{
    public CarNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
