package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class PaymentNotFoundException extends BaseException {
    public PaymentNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
