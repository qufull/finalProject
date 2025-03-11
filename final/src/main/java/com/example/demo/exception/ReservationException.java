package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class ReservationException extends BaseException{
    public ReservationException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
