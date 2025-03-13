package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class RentalPointNotFoundException extends BaseException {
    public RentalPointNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
