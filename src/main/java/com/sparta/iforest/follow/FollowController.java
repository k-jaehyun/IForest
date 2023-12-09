package com.sparta.iforest.follow;

import com.sparta.iforest.CommonResponseDto;
import com.sparta.iforest.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<CommonResponseDto> follow(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followService.follow(userId,userDetails);
    }

}
