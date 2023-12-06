package com.sparta.iforest.post;

import com.sparta.iforest.user.User;
import com.sparta.iforest.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponseDto createPost(PostRequestDto dto, String username){
        Post post = new Post(dto);

        User user = userRepository.findByUsername(username).orElseThrow();
        post.setUser(user);

        postRepository.save(post);
        return new PostResponseDto(post);
    }

}
