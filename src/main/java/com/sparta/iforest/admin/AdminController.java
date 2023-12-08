package com.sparta.iforest.admin;


import com.sparta.iforest.CommonResponseDto;
import com.sparta.iforest.post.PostRequestDto;
import com.sparta.iforest.post.PostResponseDto;
import com.sparta.iforest.post.PostService;
import com.sparta.iforest.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.concurrent.RejectedExecutionException;

@RestController
@RequestMapping("/v1/posts/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


//    //게시글 수정
//    @PutMapping("/{postId}")
//    public ResponseEntity<CommonResponseDto> updatePost(@PathVariable Long postId,
//                                                        @RequestBody PostRequestDto postRequestDto,
//                                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
//        try {
//            PostResponseDto responseDto = adminService.updatePost(postId,postRequestDto,userDetails.getUsername());
//            return ResponseEntity.ok().body(responseDto);
//        }catch (IllegalArgumentException | RejectedExecutionException e){
//            return ResponseEntity.badRequest().body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
//        }
//    }


    //게시글 삭제
    @DeleteMapping("/{postId}")
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





}
