package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class ReservationNotFoundException extends BaseException{
    public ReservationNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
