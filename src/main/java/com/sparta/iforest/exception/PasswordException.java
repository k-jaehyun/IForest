package com.sparta.iforest.exception;

import org.springframework.http.HttpStatus;

public class PasswordException extends Exception {

    private HttpStatus httpStatus;

    public PasswordException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

