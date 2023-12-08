package com.sparta.iforest.post;


import com.sparta.iforest.CommonResponseDto;
import com.sparta.iforest.comment.Comment;
import com.sparta.iforest.comment.CommentResponseDto;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
