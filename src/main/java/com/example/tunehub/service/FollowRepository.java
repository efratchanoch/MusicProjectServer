package com.example.tunehub.service;

import com.example.tunehub.model.EFollowStatus;
import com.example.tunehub.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long id, Long targetUserId);

    Optional<Follow> findByFollowerIdAndFollowingIdAndStatus(Long followerId, Long id, EFollowStatus eFollowStatus);
}
