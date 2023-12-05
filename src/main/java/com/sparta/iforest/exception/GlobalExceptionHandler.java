package com.sparta.iforest.exception;

import com.sparta.iforest.CommonResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<CommonResponseDto> IllegelArgumentExceptionHandler(IllegalArgumentException ex) {
        CommonResponseDto commonResponseDto = new CommonResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(commonResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<CommonResponseDto> NoSuchElementExceptionHandler(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponseDto(ex.getMessage(),HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler({FieldErrorException.class})
    public ResponseEntity<FieldErrorResponseDto> FieldErrorException(FieldErrorException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new FieldErrorResponseDto(
                        ex.getMessage(),
                        ex.getStatusCode(),
                        ex.getFieldErrorDtoList()
                ));
    }
}
