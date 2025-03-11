package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class CredentialNotFoundException extends BaseException{
    public CredentialNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
