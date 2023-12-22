package com.sparta.iforest.likes;

import com.sparta.iforest.comment.CommentRequestDto;
import com.sparta.iforest.post.Post;
import com.sparta.iforest.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "likes")
@NoArgsConstructor
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private Long receivedLikeCnt;

    public Likes(User user, Post post, Long receivedLikeCnt) {
        this.post = post;
        this.user = user;
        this.receivedLikeCnt = receivedLikeCnt;
    }
    public void addLikes(){
        this.receivedLikeCnt++;
    }
}
