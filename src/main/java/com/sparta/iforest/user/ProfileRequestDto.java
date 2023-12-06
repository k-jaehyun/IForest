package com.sparta.iforest.user;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class ProfileRequestDto {

    @Email(message = "이메일 형식에 맞게 적어주세요")
    @NotBlank
    private String email;

    @Pattern(regexp = "^[a-z]+[0-9]*$", message = "닉네임 허용문자에 맞게 적어주세요")
    @Size(min = 4, max = 10, message = "닉네임 4자 이상 10자 이하이어야 합니다")
    @NotBlank
    private String user_name;


    @Size(max = 300, message = "최대 300자 까지 입니다.")
    private String introduction;

    public HashMap<String, String> fieldChangeCheck(User user) {
        HashMap<String, String> map = new HashMap<>();

        if (!(this.email.equals(user.getEmail()))) {
            map.put("email", this.email);
        } else if (!(this.user_name.equals(user.getUsername()))) {
            map.put("user_name", this.user_name);
        }
        else if (!(this.introduction.equals(user.getUsername()))) {
            map.put("introduction", this.introduction);
        }
        return map;
    }
}
