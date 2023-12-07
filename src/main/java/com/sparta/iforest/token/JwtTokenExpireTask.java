package com.sparta.iforest.token;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtTokenExpireTask {

    private final TokenRepository tokenRepository;

    @Transactional
    @Scheduled(fixedRate = 60 * 1000)
    public void cleanupExpireTokens() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        tokenRepository.deleteByCreatedTimeBefore(oneHourAgo);
    }

}
