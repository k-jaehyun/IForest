package com.sparta.iforest.comment;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long postId;
    private String text;
}
