package com.sparta.iforest.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class FieldErrorException extends RuntimeException{
    private final Integer status;
    private final List<FieldErrorDto> fieldErrorDtoList;
    public FieldErrorException(String message, Integer statusCode, List<FieldErrorDto> fieldErrorDtoList) {
        super(message);
        this.status=statusCode;
        this.fieldErrorDtoList=fieldErrorDtoList;
    }
}
