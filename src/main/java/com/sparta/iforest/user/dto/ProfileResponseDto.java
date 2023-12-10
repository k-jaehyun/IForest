package com.sparta.iforest.user.dto;

import com.sparta.iforest.user.User;
import lombok.Getter;

@Getter
public class ProfileResponseDto {

    private Long id;

    private String email;

    private String username;

    private String introduction;

    public ProfileResponseDto(User userId) {
        this.id = userId.getId();
        this.email = userId.getEmail();
        this.username = userId.getUsername();
        this.introduction = userId.getIntroduction();
    }

}
