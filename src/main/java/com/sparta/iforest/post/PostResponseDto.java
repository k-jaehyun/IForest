package com.sparta.iforest.post;

import lombok.Data;

import java.time.LocalDateTime;


public class PostResponseDto {

    private long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String user;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.user = post.getUser().getUsername();
    }
}
