package com.sparta.iforest.user;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void signup(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = passwordEncoder.encode(userRequestDto.getPassword());
        String email = userRequestDto.getEmail();
        String introduction = userRequestDto.getIntroduction();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 username 입니다.");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 email입니다.");
        }

        User user = new User(username, password, email, introduction);
        userRepository.save(user);
    }

    public void login(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("등록된 유저가 없습니다."));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional (readOnly = true)
    public ProfileResponseDto getProfileUpdatePage(User user) {

        return new ProfileResponseDto(user);
    }
    @Transactional
    public ProfileResponseDto updateProfile(ProfileRequestDto requestDto, User user) {

        HashMap<String, String> map = requestDto.fieldChangeCheck(user);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            switch (entry.getKey()) {

                case "username" -> nameCheck(entry.getValue());
                case "email" -> emailCheck(entry.getValue());
            }
        }

        user.profileUpdate(requestDto);

        userRepository.save(user);

        return new ProfileResponseDto(user);
    }

    private void nameCheck(String username) {
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 이름 입니다.");
        }
    }

    private void emailCheck(String email) {
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 이메일 입니다.");
        }
    }


}
