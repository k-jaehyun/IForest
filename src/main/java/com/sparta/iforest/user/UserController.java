package com.sparta.iforest.user;

import com.sparta.iforest.CommonResponseDto;
import com.sparta.iforest.Jwt.JwtUtil;
import com.sparta.iforest.exception.FieldErrorDto;
import com.sparta.iforest.exception.FieldErrorException;
import com.sparta.iforest.exception.PasswordException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

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

    @PutMapping("/(userId)/profile")
    public ResponseEntity<?> updateProfile(@RequestBody @Valid ProfileRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldErrors());
        }

        ProfileResponseDto profileResponseDto = userService.updateProfile(requestDto, userDetails.getUser());

        return ResponseEntity.ok(profileResponseDto);
    }

    @ResponseBody
    @PutMapping("/(userId)/password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid PasswordRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                            BindingResult bindingResult) throws PasswordException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldErrors());
        }

        userService.updatePassword(requestDto, userDetails.getUser());

        return ResponseEntity.ok("비밀번호 변경 완료");
    }


}
