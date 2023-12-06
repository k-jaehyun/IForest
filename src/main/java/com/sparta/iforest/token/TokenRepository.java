package com.sparta.iforest.token;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    boolean findByToken(String token);
}
