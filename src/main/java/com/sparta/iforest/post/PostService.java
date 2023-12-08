package com.sparta.iforest.post;

import com.sparta.iforest.comment.Comment;
import com.sparta.iforest.comment.CommentResponseDto;
import com.sparta.iforest.user.User;
import com.sparta.iforest.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;


    //게시글 생성
    public PostResponseDto createPost(PostRequestDto dto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        Post post = new Post(dto, user);

        postRepository.save(post);
        return new PostResponseDto(post);
    }


    //선택 게시물 조회
    public PostCommentResponseDTO getPost(Long postId) {
       Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        return new PostCommentResponseDTO(post);
    }


    //전체 게시물 조회
    public List<PostResponseDto> getAllPost() {
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        //Entity리스트 조회
        List<Post> postList = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        //Entity List -> Dto List
        postList.forEach(post -> {
            var postDto = new PostResponseDto(post);
            postResponseDtoList.add(postDto);
        });
        return postResponseDtoList;
    }

    //작성자별 게시물 조회
    public List<PostResponseDto> getPostByUser(String username) {
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        //로그인한 사용자의 username 추출 및 해당 사용자가 작성한 게시물 조회
        List<Post> postList = new ArrayList<>();
        postList = postRepository.findByUser_Username(username).orElseThrow(() -> new IllegalArgumentException("로그인한 사용자가 작성한 게시물이 없습니다."));
        //Entity List -> Dto List
        postList.forEach(post -> {
            var postDto = new PostResponseDto(post);
            postResponseDtoList.add(postDto);
        });
        return postResponseDtoList;
    }

    //게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto dto, String username) {
        //로그인한 사용자와 게시글 작성자 검증
        Post post = checkLoginUserAndPostUser(postId, username);
        //수정
//        post.setTitle(dto.getTitle());
//        post.setContent(dto.getContent());


        post.updatePost(dto);

        postRepository.save(post);
        return new PostResponseDto(post);

    }


    //게시글 삭제
    public void deletePost(Long postId, String username) {
        //로그인한 사용자와 게시글 작성자 검증
        Post post = checkLoginUserAndPostUser(postId, username);
        //delete
        postRepository.deleteById(postId);
        return;
    }

    private Post checkLoginUserAndPostUser(Long postId, String username) {
        //Id로 게시물 조회
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        //로그인한 사용자의 username 추출 및 해당 사용자가 작성한 게시물 조회
        postRepository.findByUser_Username(username).orElseThrow(() -> new IllegalArgumentException("로그인한 사용자가 작성한 게시물이 없습니다."));
        //게시글 작성자와 로그인한 작성자가 일치하는지 검증
        if (!username.equals(post.getUser().getUsername())) {
            throw new IllegalArgumentException("작성자만 게시글을 수정/삭제 할 수 있습니다.");
        }
        return post;
    }

}

