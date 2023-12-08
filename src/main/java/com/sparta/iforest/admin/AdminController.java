package com.sparta.iforest.admin;


import com.sparta.iforest.CommonResponseDto;
import com.sparta.iforest.comment.dto.CommentRequestDto;
import com.sparta.iforest.comment.dto.CommentResponseDto;
import com.sparta.iforest.post.dto.PostRequestDto;
import com.sparta.iforest.post.dto.PostResponseDto;
import com.sparta.iforest.user.UserDetailsImpl;
import com.sparta.iforest.user.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    //게시글 수정
    @PutMapping("/posts/{postId}")
    public ResponseEntity<CommonResponseDto> updatePost(@PathVariable Long postId,
                                                        @RequestBody PostRequestDto postRequestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            PostResponseDto responseDto = adminService.updatePost(postId,postRequestDto,userDetails.getUser());
            return ResponseEntity.ok().body(responseDto);
        }catch (IllegalArgumentException | RejectedExecutionException e){
            return ResponseEntity.badRequest().body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }


    //게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<CommonResponseDto> deletePost(@PathVariable Long postId,  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            adminService.deletePost(postId, userDetails.getUser());
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/v1/posts"));
            return new ResponseEntity(headers,HttpStatus.MOVED_PERMANENTLY);

        } catch (IllegalArgumentException | RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    // 댓글 수정
    @Secured(UserRoleEnum.Authority.ADMIN)
    @PutMapping("/posts/{postId}/comments/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto) {
        return adminService.updateComment(postId, commentId, requestDto);
    }

    // 유저 전체 목록 조회
    @Secured(UserRoleEnum.Authority.ADMIN)
    @GetMapping("/users")
    public List<UserResponseDto> getUserList() {
        return adminService.getUserList();
    }

    // 유저 Role 변경
    @Secured(UserRoleEnum.Authority.ADMIN)
    @GetMapping("/users/{userId}/role")
    public ResponseEntity<CommonResponseDto> changeUserRole(@PathVariable Long userId) {
        return adminService.changeUserRole(userId);
    }



}
