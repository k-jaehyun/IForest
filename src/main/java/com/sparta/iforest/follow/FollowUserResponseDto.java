package com.sparta.iforest.follow;

import lombok.Getter;

@Getter
public class FollowUserResponseDto {
    private String followingUsername;
    private String introduction;

    public FollowUserResponseDto(Follow follow) {
        this.followingUsername=follow.getSender().getUsername();
        this.introduction=follow.getSender().getIntroduction();
    }
}
