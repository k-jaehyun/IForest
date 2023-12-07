package com.sparta.iforest.user;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    private String username;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()-_+=<>?/\\[\\]{}|;:'\",.]{8,15}$")
    private String password;

    @NotBlank
    @Email
    private String email;

    @Size(min = 1, max = 30)
    private String introduction;

    private String adminPW;
}
