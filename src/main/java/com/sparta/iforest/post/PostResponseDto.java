package com.sparta.iforest.post;


import com.sparta.iforest.CommonResponseDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostResponseDto extends CommonResponseDto {

    private long id;
    private String title;
    private String content;
    private String user;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.user = post.getUser().getUsername();
    }
}
