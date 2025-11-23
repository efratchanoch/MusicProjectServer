package com.example.tunehub.service;

import com.example.tunehub.model.ETargetType;
import com.example.tunehub.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUserIdAndTargetTypeAndTargetId(Long currentUserId, ETargetType targetType, Long targetId);

    boolean existsByUserIdAndTargetTypeAndTargetId(Long currentUserId, ETargetType targetType, Long targetId);
}
