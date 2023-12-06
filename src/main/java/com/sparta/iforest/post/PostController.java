package com.sparta.iforest.post;

import com.sparta.iforest.Jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<PostResponseDto> postPost(@RequestBody PostRequestDto postrequestDTO, HttpServletRequest httpServletRequest){

        PostResponseDto responseDto = postService.createPost(postrequestDTO, jwtUtil.getUsernameFromHeader(httpServletRequest));

        return ResponseEntity.status(201).body(responseDto);

    }




}
