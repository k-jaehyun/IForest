package com.sparta.iforest.post.dto;

import com.sparta.iforest.CommonResponseDto;
import com.sparta.iforest.comment.Comment;
import com.sparta.iforest.comment.dto.CommentResponseDto;
import com.sparta.iforest.post.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostCommentResponseDTO extends CommonResponseDto {

    private long id;
    private String title;
    private String content;
    private String user;
    private Long accessCount;
    private List<CommentResponseDto> commentResponseDtoListList;

    public PostCommentResponseDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.user = post.getUser().getUsername();
        this.commentResponseDtoListList = commentListToDtoList(post);
        this.accessCount = post.getViewCount();
    }


    public List<CommentResponseDto> commentListToDtoList(Post post) {
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        List<Comment> commentList = new ArrayList<>();
        //코멘트 리스트 반환
        commentList = post.getCommentList();
        //코멘트 리스트를 commentDto리스트로 바꾸기
        commentList.forEach(comment -> {
            var commentDto = new CommentResponseDto(comment);
            commentResponseDtoList.add(commentDto);
        });
        return commentResponseDtoList;
    }
}

