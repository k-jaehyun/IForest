package com.sparta.iforest.exception.dto;

import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
public class FieldErrorDto {
    private String errorField;
    private String correctPoint;


    public FieldErrorDto(FieldError fieldError) {
        this.errorField=fieldError.getField();
        this.correctPoint=fieldError.getDefaultMessage();
    }
}

