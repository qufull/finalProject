package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class CurrencyNotFoundException extends BaseException{
    public CurrencyNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
