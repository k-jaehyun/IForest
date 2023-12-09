package com.sparta.iforest.follow;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findBySenderIdAndReceiverId(Long id, Long id1);
}
