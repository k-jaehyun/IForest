package com.sparta.iforest.exception;

import com.sparta.iforest.CommonResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class FieldErrorResponseDto extends CommonResponseDto {
    private List<FieldErrorDto> fieldError;


    public FieldErrorResponseDto(String msg, int status, List<FieldErrorDto> fieldErrorDtoList) {
        super(msg,status);
        this.fieldError=fieldErrorDtoList;
    }
}