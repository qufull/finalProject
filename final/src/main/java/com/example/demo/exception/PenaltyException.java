package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class PenaltyException extends BaseException {
    public PenaltyException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
