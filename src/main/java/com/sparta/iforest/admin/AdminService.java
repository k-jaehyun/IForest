package com.sparta.iforest.admin;

import com.sparta.iforest.post.Post;
import com.sparta.iforest.post.PostRepository;
import com.sparta.iforest.post.dto.PostRequestDto;
import com.sparta.iforest.post.dto.PostResponseDto;
import com.sparta.iforest.user.User;
import com.sparta.iforest.user.UserRepository;
import com.sparta.iforest.user.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;



    //관리자 게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto dto, User user) {
        //포스트 가져오기
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        UserRoleEnum userRole = user.getRole();

//        if (userRole == UserRoleEnum.ADMIN){
//            //수정
            post.adminUpdatePost(dto, user.getUsername());
            postRepository.save(post);
            return new PostResponseDto(post);
//        } else {
//            throw new IllegalArgumentException ("관리자만 수정 할 수 있습니다. ");
//        }

    }




    public void deletePost(Long postId, User user) {
        //로그인한 사용자와 게시글 작성자 검증
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        UserRoleEnum userRole = user.getRole();

        if (userRole == UserRoleEnum.ADMIN){
            //delete
            postRepository.deleteById(postId);
        } else {
            throw new IllegalArgumentException ("관리자만 삭제 할 수 있습니다. ");
        }


    }



}
