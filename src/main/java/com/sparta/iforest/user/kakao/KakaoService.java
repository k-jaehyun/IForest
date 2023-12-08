package com.sparta.iforest.user.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.iforest.CommonResponseDto;
import com.sparta.iforest.Jwt.JwtUtil;
import com.sparta.iforest.Jwt.Token;
import com.sparta.iforest.Jwt.TokenRepository;
import com.sparta.iforest.user.User;
import com.sparta.iforest.user.UserRepository;
import com.sparta.iforest.user.UserRoleEnum;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Slf4j(topic = "Kakao login")
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    public ResponseEntity<CommonResponseDto> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        log.info("kakaoLogin 메서드 입성");

        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. 필요시에 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);


        // 이미 로그인 되어 있는지 확인
        if (!tokenRepository.findByUser(kakaoUser).isEmpty()) {
            Token token = tokenRepository.findByUser(kakaoUser).orElseThrow(()-> new IllegalArgumentException("존재하는데 존재하지 않는 발생할리 없는 에러"));
            String beareredToken = jwtUtil.BEARER_PREFIX+token.getTokenValue();
            response.setHeader(JwtUtil.AUTHORIZATION_HEADER,beareredToken);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new CommonResponseDto("이미 로그인 되어 있습니다.",HttpStatus.BAD_REQUEST.value()));
        }

        // header에 Role이 담긴 JwtToken탑재
        String bearerToken = jwtUtil.createToken(kakaoUser.getUsername(),kakaoUser.getRole());
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, bearerToken);

        // 토큰을 table에 넣는다.
        String token = bearerToken.substring(7);
        Token tokenObject = new Token(token,kakaoUser);
        tokenRepository.save(tokenObject);

        return ResponseEntity.status(HttpStatus.OK.value()).body(new CommonResponseDto("카카오 로그인 성공",HttpStatus.OK.value()));
    }

    private String getToken(String code) throws JsonProcessingException {
        log.info("getToken");

        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")  //kakodevelopers에 나와있음
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "1f3a8db472b612ffc21db6143a3a2a9c");
        body.add("redirect_uri", "http://localhost:8080/v1/users/kakao/callback");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity   // '이런 형태로 request해라' 라고 나와있음
                .post(uri)   // Post로 하라고 나와있음.
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기   // reponse는 이런 형태로 보내라고 나와있습니다
        ResponseEntity<String> response = restTemplate.exchange(   //여기서 restTemplate를 호출하여 카카오 서버를 호출. -> 이게 실행되면 인증코드로 토큰 요청을 하는 것!
                requestEntity,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

        log.info("getToken 나갑니다");
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        log.info("accessToken: "+accessToken);

        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());  //body는 보낼 필요가 없어서 그냥 생성만 해서 보냄

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", ");
        return new KakaoUserInfoDto(id, nickname,email);
    }

    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (kakaoUser == null) {
            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 기존 회원정보에 카카오 Id 추가
                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);  //Transactional이 필요 없게 하려고 return을 kakaoUser라는 객체로 했다.
                // -> if문 넘어가서 153줄에서 save (@Transactional 걸어버리면 하나의 오류라도 있으면 다시 롤백되므로, 오류 난건 그대로 놓고 나머지 잘 수행된건 그대로 DB에 영향을 주게 하고 싶기 때문.)
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoUserInfo.getEmail();

                kakaoUser = new User(kakaoUserInfo.getNickname(),encodedPassword,email, UserRoleEnum.User, kakaoId);
            }

            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }


}
