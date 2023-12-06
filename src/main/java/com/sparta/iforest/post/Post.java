package com.sparta.iforest.post;


import com.sparta.iforest.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn (name = "user_name")
    private User user;

    public Post(PostRequestDto dto){
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.createdAt = LocalDateTime.now();
    }

    public void setUser(User user){
        this.user = user;
    }

}
