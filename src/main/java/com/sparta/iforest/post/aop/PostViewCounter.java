package com.sparta.iforest.post.aop;

import com.sparta.iforest.post.PostService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class PostViewCounter {

    private final PostService postService;

    @Pointcut("execution(public * com.sparta.iforest.post.PostController.getPost(..)) && args(postId)")
    public void viewPostExecution(Long postId) {}

    @Before("viewPostExecution(postId)")
    public void afterViewPostExecution(Long postId) {
            postService.incrementViewCount(postId);
    }
}
