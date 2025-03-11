package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class CarNotAvailableException extends BaseException {
    public CarNotAvailableException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
