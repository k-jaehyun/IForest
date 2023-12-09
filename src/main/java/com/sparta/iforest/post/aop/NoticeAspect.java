package com.sparta.iforest.post.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.iforest.post.PostService;
import com.sparta.iforest.post.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class NoticeAspect {

    private final PostService postService;

    @Pointcut("execution(public * com.sparta.iforest.post.PostController.getAllPost(..))")
    public void addNoticeToResponseBody() {}

    @AfterReturning(value = "addNoticeToResponseBody()", returning = "responseEntity")
    public ResponseEntity<List<PostResponseDto>> aftergetPostsExecution(ResponseEntity<List<PostResponseDto>> responseEntity) throws JsonProcessingException {
        List<PostResponseDto> noticePostList = postService.getNoticeList();

            List<PostResponseDto> responseBody = responseEntity.getBody();
            responseBody.addAll(0, noticePostList); // 공지글을 응답 바디의 앞에 추가

            return ResponseEntity.ok().body(responseBody);
    }
}
