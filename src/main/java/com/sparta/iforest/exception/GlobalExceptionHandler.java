package com.sparta.iforest.exception;

import com.sparta.iforest.CommonResponseDto;
import com.sparta.iforest.exception.dto.FieldErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<CommonResponseDto> IllegelArgumentExceptionHandler(IllegalArgumentException ex) {
        CommonResponseDto commonResponseDto = new CommonResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(commonResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({FieldErrorException.class})
    public ResponseEntity<FieldErrorResponseDto> FieldErrorException(FieldErrorException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new FieldErrorResponseDto(
                        ex.getMessage(),
                        ex.getStatus(),
                        ex.getFieldErrorDtoList()
                ));
    }
}
