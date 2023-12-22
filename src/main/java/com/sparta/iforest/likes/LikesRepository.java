package com.sparta.iforest.likes;


import com.sparta.iforest.post.Post;
import com.sparta.iforest.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Likes findByPostIdAndUserId(Long postId, Long memberId);
    Object countByPostIdAndIsLikeTrue(Long postId);
}