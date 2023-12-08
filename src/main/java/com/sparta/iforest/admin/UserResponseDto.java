package com.sparta.iforest.admin;

import com.sparta.iforest.user.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    Long userId;
    String username;

    public UserResponseDto(User user) {
        this.userId=user.getId();
        this.username=user.getUsername();
    }
}
