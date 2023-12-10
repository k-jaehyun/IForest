package com.sparta.iforest.user.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "4~10자리의 소문자 또는 숫자만 등록 가능합니다..")
    private String username;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()-_+=<>?/\\[\\]{}|;:'\",.]{8,15}$", message = "8~15자리의 영문,숫자,특수문자만 등록 가능합니다.")
    private String password;

    @NotBlank
    @Email
    private String email;

    @Size(min = 1, max = 30)
    private String introduction;

    private String adminPW;
}
