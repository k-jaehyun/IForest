package com.sparta.iforest.follow;

import com.sparta.iforest.CommonResponseDto;
import com.sparta.iforest.user.User;
import com.sparta.iforest.user.UserDetailsImpl;
import com.sparta.iforest.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public ResponseEntity<CommonResponseDto> follow(Long userId, UserDetailsImpl userDetails) {
        User receiver = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 유저 아이디 입니다."));
        User sender = userDetails.getUser();
        if (sender.getId().equals(userId)) {
            throw new IllegalArgumentException("자기 자신을 팔로우 할 수 없습니다.");
        }


        Follow alreadyExistingFollow = followRepository.findBySenderIdAndReceiverId(sender.getId(),receiver.getId()).orElse(null);
        if (alreadyExistingFollow!=null) {
            followRepository.delete(alreadyExistingFollow);
            return ResponseEntity.ok().body(new CommonResponseDto("언팔로우 완료..!", HttpStatus.OK.value()));
        }

        Follow follow = new Follow(receiver,sender);
        followRepository.save(follow);

        return ResponseEntity.ok().body(new CommonResponseDto("팔로우 완료!", HttpStatus.OK.value()));
    }

    public List<FollowUserResponseDto> getUsersFollowed(Long userId) {
        User receiver = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 유저 아이디 입니다."));
        return followRepository.findAllByReceiverId(receiver.getId()).stream().map(FollowUserResponseDto::new).toList();
    }

    public List<FollowUserResponseDto> getUsersFollowing(Long userId, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 유저 아이디 입니다."));
        if (!user.getId().equals(userDetails.getUser().getId())) {
            throw new IllegalArgumentException("팔로잉 목록은 자기 자신만 확인 할 수 있습니다.");
        }

        return followRepository.findAllBySenderId(userId).stream().map(FollowUserResponseDto::new).toList();
    }
}
