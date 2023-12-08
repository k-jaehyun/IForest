package com.sparta.iforest.comment;

import com.sparta.iforest.Timestamped;
import com.sparta.iforest.comment.dto.CommentRequestDto;
import com.sparta.iforest.post.Post;
import com.sparta.iforest.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comment")
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(CommentRequestDto requestDto, User user, Post post) {
        this.content = requestDto.getContent();
        this.post = post;
        this.user = user;
    }



    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

}
