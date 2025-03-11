package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class DriverLicenseCreationException extends BaseException{
    public DriverLicenseCreationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
