package com.sparta.iforest.user;

import lombok.Getter;

@Getter
public class ProfileResponseDto {

    private Long id;

    private String email;

    private String user_name;

    private String introduction;

    public ProfileResponseDto(User userId) {
        this.id = userId.getId();
        this.email = userId.getEmail();
        this.user_name = userId.getUsername();
        this.introduction = userId.getIntroduction();
    }

}
