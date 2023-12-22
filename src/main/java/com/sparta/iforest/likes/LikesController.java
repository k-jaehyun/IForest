package com.sparta.iforest.likes;

import com.sparta.iforest.CommonResponseDto;
import com.sparta.iforest.post.PostService;
import com.sparta.iforest.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @GetMapping("/{postId}/likes")
    public ResponseEntity<CommonLikeResponseDto> postLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommonLikeResponseDto("로그인 한 사용자만 접근 할 수 있습니다. 혹은 유효한 토큰인지 확인하십시오.", HttpStatus.UNAUTHORIZED.value(),null));
        }else {
            return likesService.postLike(postId, userDetails.getUser());
        }
    }

//    @GetMapping("/delete/{postId}")
//    public String deleteLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        likesService.deleteLikes(postId);
//        return "redirect:/boards/" + postService.getPost(postId) + "/" + postId;
//    }
}