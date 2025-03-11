package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class CredentialException extends BaseException {
    public CredentialException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
