package com.sparta.iforest.comment;

import com.sparta.iforest.post.Post;
import com.sparta.iforest.post.PostRepository;
import com.sparta.iforest.user.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponseDto createComment(Long postId , CommentRequestDto requestDto, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalIdentifierException("선택한 게시글은 존재하지 않습니다."));
        Comment saveComment = commentRepository.save(new Comment(requestDto, user, post));
        return new CommentResponseDto(saveComment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        // DB에 존재하는지 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalIdentifierException("선택한 댓글이 존재하지 않습니다."));
        if (!isAccessableUser(user, comment.getUser())) {
            throw new IllegalIdentifierException("작성자만 삭제/수정할 수 있습니다");
        }

        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }

    public ResponseEntity deleteComment(Long commentId, User user) {
        // DB에 존재하는지 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalIdentifierException("선택한 댓글이 존재하지 않습니다."));
        if (!isAccessableUser(user, comment.getUser())) {
            throw new IllegalIdentifierException("작성자만 삭제/수정할 수 있습니다");
        }

        commentRepository.delete(comment);
        return new ResponseEntity("댓글이 삭제되었습니다", HttpStatus.OK);
    }

    private boolean isAccessableUser(User target_user, User access_user) {
        if (target_user == null || access_user == null) {
            return false;
        }

         if (/*access_user.getRole() == UserRoleEnum.ADMIN*/ access_user.getId().equals(target_user.getId())) {
             return true;

         }
        return false;
    }
}
