package com.sparta.iforest.comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String username;
    private String text;
    private String target;
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.username = comment.getUser().getUsername();
        this.text = comment.getText();
        this.createdAt = comment.getCreatedAt();

        if (comment.getParent() != null) {
            this.target = comment.getParent().getUser().getUsername();
        }
    }
}
