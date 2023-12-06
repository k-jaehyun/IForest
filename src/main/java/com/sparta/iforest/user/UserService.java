package com.sparta.iforest.user;

import com.sparta.iforest.CommonResponseDto;
import com.sparta.iforest.Jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();
        String introduction = requestDto.getIntroduction();
        String adminPW = requestDto.getAdminPW();

        //회원 중복 확인
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 username 입니다.");
        }
        //email 중복 확인
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 email입니다.");
        }
        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.User;
        if (adminPW!=null && !adminPW.isEmpty()) { // 관리자 암호를 보내주지 않거나 빈칸인 경우에는 if문에 들어가지 않습니다
            if (!jwtUtil.validateAdminPW(adminPW)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, email, introduction, role);
        userRepository.save(user);
    }

    public ResponseEntity<CommonResponseDto> login(LoginRequestDto requestDto, HttpServletResponse response) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 등록된 유저인지 확인
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("등록된 유저가 없습니다."));

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // header에 Role이 담긴 JwtToken탑재
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(requestDto.getUsername(),user.getRole()));

        // 관리자/일반유저 로그인 메세지 출력
        if (user.getRole()==UserRoleEnum.ADMIN) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(new CommonResponseDto("관리자님 환영합니다.",HttpStatus.OK.value()));
        } else {
            return ResponseEntity.status(HttpStatus.OK.value()).body(new CommonResponseDto("로그인 성공",HttpStatus.OK.value()));
        }
    }
}
