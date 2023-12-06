package com.sparta.iforest.token;

import com.sparta.iforest.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="token")
@Getter
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String tokenValue;

    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne
    private User user;

    public Token(String token, User user) {
        this.tokenValue=token;
        this.user=user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
