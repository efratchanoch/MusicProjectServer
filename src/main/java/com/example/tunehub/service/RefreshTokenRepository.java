package com.example.tunehub.service;

import com.example.tunehub.model.RefreshToken;
import com.example.tunehub.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
//    // מציאת טוקן רענון לפי המחרוזת שלו
//    Optional<RefreshToken> findByToken(String token);
//
//    // מחיקת טוקן רענון של משתמש ספציפי (למשל, בלוג-אאוט)
//    @Modifying
//    int deleteByUser(Users user);
}