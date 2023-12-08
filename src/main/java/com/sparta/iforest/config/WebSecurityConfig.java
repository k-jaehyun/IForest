package com.sparta.iforest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.iforest.CommonResponseDto;
import com.sparta.iforest.Jwt.JwtAuthorizationFilter;
import com.sparta.iforest.Jwt.JwtUtil;
import com.sparta.iforest.Jwt.TokenRepository;
import com.sparta.iforest.user.UserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import com.sparta.iforest.user.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper;

    private final TokenRepository tokenRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService, objectMapper, tokenRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers("/v1/users/signup").permitAll() //모든 권한 허용
                        .requestMatchers("/v1/users/login").permitAll()
                        .requestMatchers("/v1/users/logout").permitAll()
                        .requestMatchers("/v1/users/kakao/callback/**").permitAll()
                        .requestMatchers("/v1/admin/post").hasRole(UserRoleEnum.ADMIN.toString())
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        // 필터 관리
        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 토큰 인증 오류 처리
        http.exceptionHandling(config -> {
            config.authenticationEntryPoint(errorPoint());
            config.accessDeniedHandler(accessDeniedHandler());
        });

        return http.build();
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            CommonResponseDto commonResponseDto = new CommonResponseDto(ex.getMessage(), HttpStatus.FORBIDDEN.value());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(commonResponseDto));
        };
    }

    private AuthenticationEntryPoint errorPoint() {
        return (request, response, authException) -> {
            authException.printStackTrace();
            CommonResponseDto commonResponseDto = new CommonResponseDto("유효한 토큰이 아닙니다.", HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(commonResponseDto));
        };
    }
}