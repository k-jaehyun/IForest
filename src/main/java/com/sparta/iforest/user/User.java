package com.sparta.iforest.user;

import com.sparta.iforest.follow.Follow;
import com.sparta.iforest.user.dto.ProfileRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "user")
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

    @Column
    private Long kakaoId;

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

    public User(String username, String password, String email, UserRoleEnum role, Long kakaoId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.kakaoId=kakaoId;
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

    public void profileUpdate(ProfileRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.email = requestDto.getEmail();
        this.introduction = requestDto.getIntroduction();
    }
    public void passwordUpdate(String password) {
        this.password = password;
    }

    public void updateRole(UserRoleEnum userRoleEnum) {
        this.role=userRoleEnum;
    }

    public void banUser() {
        this.role=UserRoleEnum.BAN;
    }
}
