package com.sparta.iforest.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<List<Post>> findByUser_Username(String username);

    List<Post> findAllByIsNoticeTrue();

}
