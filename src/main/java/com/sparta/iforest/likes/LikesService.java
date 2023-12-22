package com.sparta.iforest.likes;

import com.sparta.iforest.post.Post;
import com.sparta.iforest.post.PostRepository;
import com.sparta.iforest.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final PostRepository postRepository;

    @Transactional
    public ResponseEntity<CommonLikeResponseDto> postLike(Long postId, User user) {

        try {
            Post post = findPostById(postId);
            if (post.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("본인의 게시글 입니다.");
            }
            Likes likes = likesRepository.findByPostIdAndUserId(post.getId(), user.getId());
            if (likes == null) {
                likesRepository.save(new Likes(user, post, 1L));
            } else {
                likes.addLikes();
            }
            Long likescount = (long) likesRepository.countByPostIdAndIsLikeTrue(post.getId());

            return ResponseEntity.ok().body(new CommonLikeResponseDto("좋아요 성공", HttpStatus.OK.value(), likescount));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new CommonLikeResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
    }

    private Post findPostById (Long id){
        return postRepository.findById(id).orElseThrow(() ->new IllegalArgumentException("존재하지 않는 postId입니다."));
    }
    }



//    @Transactional
//    public void deleteLikes(Long postId) {
//        Post post = postRepository.findById(postId).get();
//        User loginUser = userRepository.findByLoginId(loginId).get();
//        User postUser = post.getUser();
//
//        // 자신이 누른 좋아요가 아니라면
//        if (!postUser.equals(loginUser)) {
//            postUser.likesChange(postUser.getReceivedLikeCnt() - 1);
//        }
//        post.likesChange(post.getLikeCnt() - 1);
//
//        likesRepository.existsByUserIdAndPostId(loginId, postId);
//    }
//
//    public Boolean checkLike(String loginId, Long postId) {
//        return likesRepository.existsByUserLoginIdAndPostId(loginId, postId);
//    }
//}