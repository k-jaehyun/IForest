package com.sparta.iforest.admin;

import com.sparta.iforest.CommonResponseDto;
import com.sparta.iforest.Jwt.TokenRepository;
import com.sparta.iforest.comment.Comment;
import com.sparta.iforest.comment.CommentRepository;
import com.sparta.iforest.comment.dto.CommentRequestDto;
import com.sparta.iforest.comment.dto.CommentResponseDto;
import com.sparta.iforest.post.Post;
import com.sparta.iforest.post.PostRepository;
import com.sparta.iforest.post.dto.PostRequestDto;
import com.sparta.iforest.post.dto.PostResponseDto;
import com.sparta.iforest.user.User;
import com.sparta.iforest.user.UserDetailsImpl;
import com.sparta.iforest.user.UserRepository;
import com.sparta.iforest.user.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final TokenRepository tokenRepository;



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

    @Transactional
    public CommentResponseDto updateComment(Long postId, Long commentId, CommentRequestDto requestDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        if (comment.getPost()!=post) {
            throw new IllegalArgumentException("해당 게시글에 존재하지 않는 댓글입니다.");
        }
        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }

    public void deleteComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        if (comment.getPost()!=post) {
            throw new IllegalArgumentException("해당 게시글에 존재하지 않는 댓글입니다.");
        }
        commentRepository.delete(comment);
    }

    public List<CommentResponseDto> getCommentList() {
        return commentRepository.findAll().stream().map(CommentResponseDto::new).toList();
    }


    public List<UserResponseDto> getUserList() {
        return userRepository.findAll().stream().map(UserResponseDto::new).toList();
    }

    @Transactional
    public ResponseEntity<CommonResponseDto> changeUserRole(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 user_id 입니다."));

        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            user.updateRole(UserRoleEnum.User);
        } else {
            user.updateRole(UserRoleEnum.ADMIN);
        }

        return ResponseEntity.ok().body(new CommonResponseDto(user.getRole().getAuthority()+"로 변경되었습니다.",HttpStatus.OK.value()));
    }

    public ResponseEntity<CommonResponseDto> deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 user_id 입니다."));
        String username = user.getUsername();

        if(!tokenRepository.findByUser_Id(userId).isEmpty()) {
            tokenRepository.delete(tokenRepository.findByUser_Id(userId).orElseThrow());
        }
        userRepository.delete(user);

        return ResponseEntity.ok().body(new CommonResponseDto("username: "+username+"가 삭제되었습니다",HttpStatus.OK.value()));
    }

    public ResponseEntity<CommonResponseDto> postNotice(PostRequestDto requestDto, UserDetailsImpl userDetails) {
        Post post = new Post(requestDto, userDetails.getUser());
        post.notifyPost();
        postRepository.save(post);
        return ResponseEntity.ok().body(new CommonResponseDto("공지글 등록 완료",HttpStatus.OK.value()));
    }

    @Transactional
    public ResponseEntity<CommonResponseDto> banUser(Long userId) {
        User user= userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 user_id 입니다."));
        user.banUser();
        return ResponseEntity.ok().body(new CommonResponseDto(user.getUsername()+"가 ban 되었습니다.",HttpStatus.OK.value()));
    }
}
