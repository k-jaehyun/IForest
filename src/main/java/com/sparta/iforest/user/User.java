package com.sparta.iforest.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Setter
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String introduction;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING) // Enum Type을 DB 컬럼에 저장할 때 사용하는 애너테이션 // EnumType.STRING : Enum의 이름을 DB에 그대로 저장
    private UserRoleEnum role;

    public User(String username, String password, String email, String introduction, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.introduction = introduction;
        this.role = role;
    }
}
