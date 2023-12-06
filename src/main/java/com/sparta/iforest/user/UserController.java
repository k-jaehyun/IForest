package com.sparta.iforest.user;

import com.sparta.iforest.CommonResponseDto;
import com.sparta.iforest.Jwt.JwtUtil;
import com.sparta.iforest.exception.FieldErrorDto;
import com.sparta.iforest.exception.FieldErrorException;
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

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto> signup(@Valid @RequestBody UserRequestDto userRequestDto, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            List<FieldErrorDto> fieldErrorDtoList =fieldErrors.stream().map(FieldErrorDto::new).toList();
            throw new FieldErrorException("허용된 username 또는 password 값이 아닙니다.", HttpStatus.BAD_REQUEST.value(), fieldErrorDtoList);
        }
            userService.signup(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new CommonResponseDto("회원가입 성공",HttpStatus.OK.value()));
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto> login(@RequestBody UserRequestDto requestDto, HttpServletResponse res) {
        userService.login(requestDto);
        res.setHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(requestDto.getUsername()));
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new CommonResponseDto("로그인 성공",HttpStatus.OK.value()));
    }
    @ResponseBody
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody @Valid ProfileRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldErrors());
        }

        ProfileResponseDto profileResponseDto = userService.updateProfile(requestDto, userDetails.getUser());

        return ResponseEntity.ok(profileResponseDto);
    }


}
