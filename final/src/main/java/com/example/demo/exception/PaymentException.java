package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class PaymentException extends BaseException {
    public PaymentException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
