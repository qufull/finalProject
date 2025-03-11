package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class CarStatusException extends BaseException {
    public CarStatusException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
