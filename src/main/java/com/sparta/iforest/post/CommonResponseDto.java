package com.sparta.iforest.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude (JsonInclude.Include.NON_NULL) //null이면 json 반환X
public class CommonResponseDto {
    private String msg;
    private Integer statusCode;
}
