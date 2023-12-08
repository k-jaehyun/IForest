package com.sparta.iforest.post;


import com.sparta.iforest.Timestamped;
import com.sparta.iforest.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Post extends Timestamped {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String title;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn (name = "user_name")
    private User user;

    public Post(PostRequestDto dto, User user){
        this.user = user;
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }



    public void updatePost(PostRequestDto dto){
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }

}
