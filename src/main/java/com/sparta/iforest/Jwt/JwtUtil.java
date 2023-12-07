package com.sparta.iforest.Jwt;

import com.sparta.iforest.user.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final TokenRepository tokenRepository;
    public JwtUtil(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");
    private Key key;

    private final String adminToken = "관리자 비밀번호 가시죠";

    public static final String AUTHORIZATION_KEY = "auth";  // token의 claims에서 role에 대한 key값 ( authority를 "(String) info.get(JwtUtil.AUTHORIZATION_KEY)" 으로 받아올 수 있음)

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        // 토큰 만료시간 60분
        long TOKEN_TIME = 60 * 60 * 1000;
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)  // 토큰에도 role 정보를 넣어놨습니다!
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public boolean validateAdminPW(String PW) {
        return adminToken.equals(PW);
    }


    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER);
    }

    // JWT SubString
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }


    // 헤더에서 사용자 이름 가져오기
    public String getUsernameFromHeader(HttpServletRequest request) {
        String tokenValue = getJwtFromHeader(request);
        tokenValue = substringToken(tokenValue);
        Claims info = getUserInfoFromToken(tokenValue);
        return info.getSubject();
    }

    // 발행된 토큰을 테이블에서 만료
    @Transactional
    @Scheduled(fixedRate = 60 * 1000)  // 1분에 한번 작동
    public void cleanupExpireTokens() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        tokenRepository.deleteByCreatedTimeBefore(oneHourAgo);  // 1시간 이전 발행된 모든 토큰 삭제
    }




}
