package com.example.tunehub.service;

import com.example.tunehub.model.Comment;
import com.example.tunehub.model.Post;
import com.example.tunehub.model.SheetMusic;
import com.example.tunehub.model.Users;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public final class UsersRatingUtils {

    // קבועים לדינמיות
    private static final double TIME_WEIGHT_RECENT = 1.5;  // משקל ללייקים חדשים (פחות מחודש)
    private static final long RECENT_THRESHOLD_DAYS = 30;  // "חדש" = פחות מ-30 יום
    private static final double WILSON_CONFIDENCE = 0.95;  // רמת ביטחון ב-Wilson score
    private static final int MIN_INTERACTIONS_FOR_FULL_WEIGHT = 10;  // מינימום אינטראקציות לדירוג מלא

    public static void calculateAndSetStarRating(Users user) {
        if (user == null) return;

        double totalEngagement = calculateTotalDynamicEngagement(user);  // 🚨 דינמי עם זמן
        double starRating = convertEngagementToDynamicStarRating(totalEngagement, user.getPosts().size() + user.getSheetsMusic().size() + user.getComments().size());  // 🚨 עם Wilson

        user.setRating(starRating);
    }

    public static void calculateAndSetSheetMusicStarRating(List<SheetMusic> sheets) {
        if (sheets == null || sheets.isEmpty()) {
            return;
        }

        for (SheetMusic sheet : sheets) {
            double sheetEngagement = calculateSingleSheetDynamicEngagement(sheet);  // 🚨 דינמי
            double starRating = convertEngagementToDynamicStarRating(sheetEngagement, sheet.getLikes() + sheet.getHearts() + sheet.getDownloads());  // 🚨 עם Wilson
            sheet.setRating(starRating);
        }
    }

    public static double calculatePostStarRating(Post post, List<Comment> comments) {
        if (post == null) {
            return 0.0;
        }

        double postLikes = post.getLikes() > 0 ? post.getLikes() : 0;
        double postHearts = post.getHearts() > 0 ? post.getHearts() : 0;

        // 🚨 דינמי: משקל זמן ללייקים (הנח createdAt ב-Post)
        double directPostScore = applyTimeWeight(postLikes + postHearts, post.getDateUploaded().atStartOfDay());

        double totalCommentLikes = 0;
        if (comments != null) {
            for (Comment comment : comments) {
                double commentLikes = comment.getLikes() > 0 ? comment.getLikes() : 0;
                totalCommentLikes += applyTimeWeight(commentLikes, comment.getDateUploaded().toLocalDateTime());  // 🚨 משקל זמן לתגובות
            }
        }

        final double POST_WEIGHT = 0.7;
        final double COMMENT_WEIGHT = 0.3;
        final double COMMENT_LIKE_FACTOR = 0.5;

        double weightedCommentScore = totalCommentLikes * COMMENT_LIKE_FACTOR;

        double rawTotalScore = directPostScore + weightedCommentScore;

        // 🚨 Wilson score במקום ליניארי
        double totalInteractions = postLikes + postHearts + totalCommentLikes;
        double rating = wilsonScore(rawTotalScore / totalInteractions, (int) totalInteractions, WILSON_CONFIDENCE) * 5.0;  // ×5 לכוכבים

        return Math.min(rating, 5.0);
    }

    // 🚨 חדש: חישוב מעורבות דינמית עם משקל זמן
    private static double calculateTotalDynamicEngagement(Users user) {
        double postEngagement = 0;
        if (user.getPosts() != null) {
            postEngagement = user.getPosts().stream()
                    .mapToDouble(post -> applyTimeWeight(post.getLikes() + post.getHearts(), post.getDateUploaded().atStartOfDay()))
                    .sum();
        }

        double sheetMusicEngagement = 0;
        if (user.getSheetsMusic() != null) {
            sheetMusicEngagement = user.getSheetsMusic().stream()
                    .mapToDouble(sheet -> applyTimeWeight(sheet.getLikes() + sheet.getHearts() + sheet.getDownloads(), sheet.getDateUploaded().atStartOfDay()))
                    .sum();
        }

        double commentLikesEngagement = 0;
        if (user.getComments() != null) {
            commentLikesEngagement = user.getComments().stream()
                    .mapToDouble(comment -> applyTimeWeight(comment.getLikes(), comment.getDateUploaded().toLocalDateTime()))
                    .sum();
        }

        int followersEngagement = user.getFollowerCount() > 0 ? user.getFollowerCount() : 0;  // עוקבים ללא זמן

        return postEngagement + sheetMusicEngagement + commentLikesEngagement + followersEngagement;
    }

    // 🚨 חדש: חישוב דינמי לתו בודד
    private static double calculateSingleSheetDynamicEngagement(SheetMusic sheet) {
        double engagement = sheet.getLikes() + sheet.getHearts() + sheet.getDownloads();
        return applyTimeWeight(engagement, sheet.getDateUploaded().atStartOfDay());
    }

    // 🚨 חדש: משקל זמן – חדש = ×1.5
    private static double applyTimeWeight(double engagement, LocalDateTime createdAt) {
        if (createdAt == null) return engagement;

        long daysOld = ChronoUnit.DAYS.between(createdAt, LocalDateTime.now());
        if (daysOld <= RECENT_THRESHOLD_DAYS) {
            return engagement * TIME_WEIGHT_RECENT;
        }
        return engagement;  // ישן = משקל 1
    }

    // 🚨 חדש: Wilson Score Interval (דירוג יציב עם ביטחון)
    private static double wilsonScore(double proportion, int n, double confidence) {
        if (n == 0) return 0.0;
        double z = 1.96;  // z-score ל-95% ביטחון (תואם confidence)
        double p = proportion;  // דירוג ממוצע (לייקים / סך)
        double z2 = z * z;
        double denominator = 1 + z2 / n;
        double centreAdjustedProportion = (p + z2 / (2 * n)) / denominator;
        double leftBoundary = (centreAdjustedProportion + z * Math.sqrt(z2 / (n * n * n + n * p * (1 - p))) / denominator - z * Math.sqrt((n * p * (1 - p) + z2 * p * (1 - p) + z2 * z2 * p / 4) / (n * n * denominator * denominator))) / 2;
        return leftBoundary * 5.0;  // ×5 לכוכבים (התאמה)
    }

    // 🚨 שדרוג: המרה לכוכבים עם Wilson (במקום ליניארי פשוט)
    private static double convertEngagementToDynamicStarRating(double totalEngagement, int totalInteractions) {
        if (totalInteractions == 0) return 0.0;
        double proportion = totalEngagement / totalInteractions;  // ממוצע לייקים
        return wilsonScore(proportion, totalInteractions, WILSON_CONFIDENCE);  // Wilson ×5
    }
}
