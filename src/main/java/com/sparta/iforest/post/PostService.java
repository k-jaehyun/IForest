package com.sparta.iforest.post;

import com.sparta.iforest.user.User;
import com.sparta.iforest.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;


    //게시글 생성
    public PostResponseDto createPost(PostRequestDto dto, String username){
        Post post = new Post(dto);

        User user = userRepository.findByUsername(username).orElseThrow();
        post.setUser(user);

        postRepository.save(post);
        return new PostResponseDto(post);
    }


    //선택 게시물 조회
    public PostResponseDto getPost(Long postId){
        return new PostResponseDto(postRepository.findById(postId)
                .orElseThrow(() ->  new IllegalArgumentException("존재하지 않는 게시글입니다.")));

       // return postRepository.findById(postId).orElseThrow(() ->  new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }


    //전체 게시물 조회
    public List<PostResponseDto> getAllPost(){
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        //Entity리스트 조회
        List<Post> postList = postRepository.findAll(Sort.by(Sort.Direction.DESC,"createdAt"));
        //Entity List -> Dto List
        postList.forEach(post -> {
            var postDto = new PostResponseDto(post);
            postResponseDtoList.add(postDto);
        });
        return postResponseDtoList;
    }




}
