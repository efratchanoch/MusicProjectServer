package com.example.tunehub.service;


import com.example.tunehub.model.RefreshToken;
import com.example.tunehub.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import com.example.tunehub.service.RefreshTokenRepository;
import com.example.tunehub.service.UsersRepository;
@Service
    public class RefreshTokenService {

        @Value("${app.jwtRefreshExpirationMs}")
        private Long refreshTokenDurationMs;

        @Autowired
        private RefreshTokenRepository refreshTokenRepository;

        @Autowired
        private UsersRepository userRepository;

        public RefreshToken createRefreshToken(Long userId) {
            RefreshToken refreshToken = new RefreshToken();

            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            refreshToken.setUser(user);

            // הגדרת תאריך התפוגה הארוך (שנה)
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));

            // יצירת מחרוזת טוקן ייחודית (UUID מומלץ)
            refreshToken.setToken(UUID.randomUUID().toString());

            refreshToken = refreshTokenRepository.save(refreshToken);
            return refreshToken;
        }

        public Optional<RefreshToken> findByToken(String token) {
            return refreshTokenRepository.findByToken(token);
        }

        // בדיקה אם הטוקן פג תוקף ומחיקתו אם כן
        public RefreshToken verifyExpiration(RefreshToken token) {
            if (token.getExpiryDate().isBefore(Instant.now())) {
                refreshTokenRepository.delete(token);
                throw new RuntimeException("Refresh token was expired. Please make a new signin request");
            }
            return token;
        }
    //********תפקיד הפונקציה: מחיקת טוקן הרענון הישן של משתמש ספציפי
    @Transactional // חובה כי מדובר בפעולת שינוי (delete)
    public void deleteByUser(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        refreshTokenRepository.deleteByUser(user);
    }
}

