package com.sparta.iforest.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.iforest.CommonResponseDto;
import com.sparta.iforest.exception.FieldErrorDto;
import com.sparta.iforest.exception.FieldErrorException;
import com.sparta.iforest.user.kakao.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final KakaoService kakaoService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        // validation 검증
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            List<FieldErrorDto> fieldErrorDtoList =fieldErrors.stream().map(FieldErrorDto::new).toList();
            throw new FieldErrorException("허용된 username 또는 password 값이 아닙니다.", HttpStatus.BAD_REQUEST.value(), fieldErrorDtoList);
        }

        // sevice signup 로직
        userService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new CommonResponseDto("회원가입 성공",HttpStatus.CREATED.value()));
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse res) {
        return userService.login(requestDto, res);
    }

    @GetMapping("/logout")
    public ResponseEntity<CommonResponseDto> logout(HttpServletRequest request) {
        userService.logout(request);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new CommonResponseDto("로그아웃 성공",HttpStatus.OK.value()));
    }

    // https://kauth.kakao.com/oauth/authorize?response_type=code&client_id= ${REST_API_KEY} &redirect_uri= ${REDIRECT_URI}
    // https://kauth.kakao.com/oauth/authorize?client_id=1f3a8db472b612ffc21db6143a3a2a9c&redirect_uri=http://localhost:8080/v1/users/kakao/callback&response_type=code
    // 카카오 로그인 요청 url
    @GetMapping("/kakao/callback")
    public ResponseEntity<CommonResponseDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return kakaoService.kakaoLogin(code, response);
    }


}
