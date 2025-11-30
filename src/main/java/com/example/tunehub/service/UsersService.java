package com.example.tunehub.service;

import com.example.tunehub.model.Users;


public class UsersService {

//    private final UsersRepository userRepository;
//
//    public UsersService(UsersRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    public Users getProfileWithCalculatedRating(Long userId) {
//        Users user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found")); // נניח Exception נכון יותר
//
//        // 🛑 הפעלת הלוגיקה העסקית כאן 🛑
//        calculateAndSetStarRating(user);
//
//        return user;
//    }
//
//    public void calculateAndSetStarRating(Users user) {
//        if (user == null) return;
//
//        // א. חישוב סך המעורבות (לייקים + לבבות)
//        int totalEngagement = calculateTotalEngagement(user);
//
//        // ב. המרת הציון לדירוג כוכבים (0.0 עד 5.0)
//        double starRating = convertEngagementToStarRating(totalEngagement);
//
//        // ג. הגדרת הדירוג בשדה הזמני של אובייקט ה-Users
//        user.setRating(starRating);
//    }
//
//    /**
//     * מחשבת את סך הלייקים והלבבות מכל הפוסטים והתווים.
//     */
//    private int calculateTotalEngagement(Users user) {
//        // ודא שהאוספים של הפוסטים והתווים נטענים (בדרך כלל @OneToMany)
//        int postEngagement = 0;
//        if (user.getPosts() != null) {
//            postEngagement = user.getPosts().stream()
//                    .mapToInt(post -> post.getLikes() + post.getHearts())
//                    .sum();
//        }
//
//        int sheetMusicEngagement = 0;
//        if (user.getSheetsMusic() != null) {
//            sheetMusicEngagement = user.getSheetsMusic().stream()
//                    .mapToInt(sheet -> sheet.getLikes() + sheet.getHearts())
//                    .sum();
//        }
//
//        return postEngagement + sheetMusicEngagement;
//    }
//
//    /**
//     * ממירה את סך המעורבות לדירוג כוכבים (0.0 - 5.0).
//     */
//    private double convertEngagementToStarRating(int totalEngagement) {
//        // סף מקסימלי: 500 נקודות = 5 כוכבים
//        final int MAX_ENGAGEMENT_FOR_5_STARS = 500;
//
//        if (totalEngagement >= MAX_ENGAGEMENT_FOR_5_STARS) {
//            return 5.0;
//        }
//
//        // חישוב ציון ליניארי: כל 100 נקודות שוות לכוכב
//        double rawRating = (double)totalEngagement / (MAX_ENGAGEMENT_FOR_5_STARS / 5.0);
//
//        // החזרת הדירוג, מוגבל ל-5.0
//        return Math.min(rawRating, 5.0);
//    }
}

