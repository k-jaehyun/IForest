package com.sparta.iforest.token;

import com.sparta.iforest.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByTokenValue(String token);

    Optional<Token> findByUser(User user);

    void deleteByCreatedTimeBefore(LocalDateTime oneHourAgo);
}
