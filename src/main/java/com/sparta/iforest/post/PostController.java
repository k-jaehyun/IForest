package com.sparta.iforest.post;

import com.sparta.iforest.CommonResponseDto;
import com.sparta.iforest.Jwt.JwtUtil;
import com.sparta.iforest.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    //private final CommonResponseDto commonResponseDto;


    //게시글 생성
    @PostMapping
    public ResponseEntity<PostResponseDto> postPost(@RequestBody PostRequestDto postrequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails){
        PostResponseDto responseDto = postService.createPost(postrequestDTO, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(responseDto);
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
        List<PostResponseDto> postResponseDtoList= postService.getAllPost();
        return ResponseEntity.ok().body(postResponseDtoList);
    }

    //작성자별 게시물 조회
    @GetMapping("/request-param")
    public ResponseEntity<List<PostResponseDto>> getPostByUser(@RequestParam ("user") String username){
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        postResponseDtoList = postService.getPostByUser(username);
        return ResponseEntity.ok().body(postResponseDtoList);
        //캐치 해야함
    }

    //게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<CommonResponseDto> updatePost(@PathVariable Long postId,
                                                        @RequestBody PostRequestDto postRequestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            PostResponseDto responseDto = postService.updatePost(postId,postRequestDto,userDetails.getUsername());
            return ResponseEntity.ok().body(responseDto);
        }catch (IllegalArgumentException |  RejectedExecutionException e){
            return ResponseEntity.badRequest().body(new CommonResponseDto(e.getMessage(),HttpStatus.BAD_REQUEST.value()));
        }
    }


    //게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponseDto> deletePost(@PathVariable Long postId,  @AuthenticationPrincipal UserDetailsImpl userDetails){
        try{
            postService.deletePost(postId, userDetails.getUsername());
            return redirectToGetAllPost();
        }catch (IllegalArgumentException | RejectedExecutionException e){
            return ResponseEntity.badRequest().body(new CommonResponseDto(e.getMessage(),HttpStatus.BAD_REQUEST.value()));
        }



    }

    //기본 페이지로 리턴
    private ResponseEntity<CommonResponseDto> redirectToGetAllPost() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/posts"));
        return new ResponseEntity(headers,HttpStatus.MOVED_PERMANENTLY);
    }


}
