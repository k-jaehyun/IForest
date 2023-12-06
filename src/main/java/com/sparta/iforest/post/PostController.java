package com.sparta.iforest.post;

import com.sparta.iforest.Jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@RestController
@RequestMapping ("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;


    //게시글 생성
    @PostMapping
    public ResponseEntity<PostResponseDto> postPost(@RequestBody PostRequestDto postrequestDTO, HttpServletRequest httpServletRequest){
        PostResponseDto responseDto = postService.createPost(postrequestDTO, jwtUtil.getUsernameFromHeader(httpServletRequest));
        return ResponseEntity.status(201).body(responseDto);
    }

    //선택 게시글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponseDto> getPost(@PathVariable Long postId){
       try {
           PostResponseDto responseDto = postService.getPost(postId);
           return ResponseEntity.ok().body(responseDto);

       } catch (IllegalArgumentException e){
           return ResponseEntity.badRequest().body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
       }
    }

    //게시물 전체 조회
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPost(){
        List<PostResponseDto> postResponseDtoList= new ArrayList<>();
        postResponseDtoList = postService.getAllPost();
        return ResponseEntity.ok().body(postResponseDtoList);
    }


    //게시글 수정


    //게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponseDto> deletePost(@PathVariable Long postId, HttpServletRequest httpServletRequest){
        try{
            postService.deletePost(postId, jwtUtil.getUsernameFromHeader(httpServletRequest));
            return redirectToGetAllPost();
        }catch (IllegalArgumentException | RejectedExecutionException e){
            return ResponseEntity.badRequest().body(new CommonResponseDto(e.getMessage(),HttpStatus.BAD_REQUEST.value()));
        }



    }

    private ResponseEntity<CommonResponseDto> redirectToGetAllPost() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/posts"));
        return new ResponseEntity(headers,HttpStatus.MOVED_PERMANENTLY);
    }


}
