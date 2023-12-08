package com.sparta.iforest.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PasswordRequestDto {

    @Pattern(regexp = "[a-zA-Z0-9]*$", message = "비밀번호 허용 문자에 맞게 해주세요")
    @Size(min = 8, max = 15, message = "비밀번호 8자 이상 15자 이하이어야 합니다")
    @NotBlank
    private String currentPassword;

    @Pattern(regexp = "[a-zA-Z0-9]*$", message = "비밀번호 허용 문자에 맞게 해주세요")
    @Size(min = 8, max = 15, message = "비밀번호 8자 이상 15자 이하이어야 합니다")
    @NotBlank
    private String newPassword;

    @NotBlank
    private String passwordCheck;
}
