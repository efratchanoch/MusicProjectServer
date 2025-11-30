package com.example.tunehub.service;

import com.example.tunehub.model.Comment;
import com.example.tunehub.model.Post;
import com.example.tunehub.model.SheetMusic;
import com.example.tunehub.model.Users;

import java.util.List;

public final class UsersRatingUtils {

    private void UserRatingUtils() {}

    /**
     * פונקציה ראשית שנקראת מה-Controller.
     * סטטית כדי לאפשר קריאה ישירה.
     */
    public static void calculateAndSetStarRating(Users user) {
        if (user == null) return;

        int totalEngagement = calculateTotalEngagement(user);
        double starRating = convertEngagementToStarRating(totalEngagement);

        user.setRating(starRating);
    }

    private static int calculateSingleSheetEngagement(SheetMusic sheet) {
        int likes = sheet.getLikes() >  0 ? sheet.getLikes() : 0;
        int hearts = sheet.getHearts() >  0 ? sheet.getHearts() : 0;
        int downdlows = sheet.getDownloads()  > 0 ? sheet.getDownloads()  : 0;
        return likes + hearts + downdlows;
    }
    public static void calculateAndSetSheetMusicStarRating(List<SheetMusic> sheets) {
        if (sheets == null || sheets.isEmpty()) {
            return;
        }

        // ⭐️ עוברים בלולאה על כל גיליון מוזיקה ברשימה ⭐️
        for (SheetMusic sheet : sheets) {

            // 1. חישוב ה-Engagement עבור הגיליון הנוכחי בלבד
            int sheetEngagement = calculateSingleSheetEngagement(sheet);

            // 2. המרת ה-Engagement לדירוג כוכבים (לדוגמה, מתוך 5)
            double starRating = convertEngagementToStarRating(sheetEngagement);

            // 3. עדכון שדה הדירוג בגיליון הבודד
            sheet.setRating(starRating);
        }
    }


    public static double calculatePostStarRating(Post post, List<Comment> comments) {
        if (post == null) {
            return 0.0;
        }

        // --- 1. חישוב ציון הפוסט הישיר (Post Score) ---
        // נניח ש"לבבות" ו"לייקים" על הפוסט שווים בערכם.
        int postLikes = post.getLikes() > 0 ? post.getLikes() : 0;
        int postHearts = post.getHearts() > 0 ? post.getHearts() : 0;

        // סכום האינטראקציות הישירות על הפוסט
        int directPostScore = postLikes + postHearts;

        // --- 2. חישוב ציון התגובות המשוקלל (Comments Score) ---
        int totalCommentLikes = 0;
        if (comments != null) {
            for (Comment comment : comments) {
                int commentLikes = comment.getLikes() > 0 ? comment.getLikes() : 0;
                totalCommentLikes += commentLikes;
            }
        }

        // --- 3. קביעת משקלים ---

        // ניתן לשלוט על חשיבות התגובות (לדוגמה: 70% לפוסט, 30% לתגובות)
        final double POST_WEIGHT = 0.7;
        final double COMMENT_WEIGHT = 0.3;

        // --- 4. חישוב ציון האינגייג'מנט הכולל (Total Engagement) ---

        // נכפיל את לייקי התגובות במקדם נמוך יותר, כי אינטראקציה על תגובה פחות "חזקה"
        // מאינטראקציה ישירה על הפוסט. נגדיר מקדם של 0.5 ללייק על תגובה.
        final double COMMENT_LIKE_FACTOR = 0.5;

        double weightedCommentScore = totalCommentLikes * COMMENT_LIKE_FACTOR;

        // הציון הגולמי הכולל
        double rawTotalScore = directPostScore + weightedCommentScore;

        // --- 5. המרה לדירוג כוכבים (0-5) ---

        // נגדיר מהו הציון הכולל ששווה ל-5 כוכבים. זהו הקבוע שיקבע את "סולם הדירוג" שלך.
        final double MAX_SCORE_FOR_5_STARS = 1500.0; // לדוגמה, סך 1500 אינטראקציות = 5 כוכבים
        final double MAX_RATING = 5.0;

        double rating = (rawTotalScore / MAX_SCORE_FOR_5_STARS) * MAX_RATING;

        // הגבלת הדירוג להיות מקסימום 5 כוכבים
        return Math.min(rating, MAX_RATING);
    }
    /**
     * מחשב את סך המעורבות עבור תווי נגינה (Sheet Music)
     * לפי: לייקים + לבבות + הורדות.
     */
    private static int calculateSheetMusicTotalEngagement(SheetMusic sheet) {
        int sheetEngagement = 0;

        // ודא שכל השדות זמינים (ומטפל ב-null אם הטיפוסים הם Integer)
        if (sheet.getLikes() >  0) {
            sheetEngagement += sheet.getLikes();
        }
        if (sheet.getHearts() >  0) {
            sheetEngagement += sheet.getHearts();
        }

        // ⭐️ הוספת ההורדות
        if (sheet.getDownloads() >  0) {
            sheetEngagement += sheet.getDownloads();
        }

        return sheetEngagement;
    }

    private static int calculateTotalEngagement(Users user) {
        // 1. מעורבות מפוסטים
        int postEngagement = 0;
        if (user.getPosts() != null) {
            postEngagement = user.getPosts().stream()
                    .mapToInt(post -> post.getLikes() + post.getHearts())
                    .sum();
        }

        // 2. מעורבות מתווי נגינה (Sheet Music)
        int sheetMusicEngagement = 0;
        if (user.getSheetsMusic() != null) {
            sheetMusicEngagement = user.getSheetsMusic().stream()
                    .mapToInt(sheet -> sheet.getLikes() + sheet.getHearts())
                    .sum();
        }

        // 3. ⭐️ מעורבות מתגובות (Comments Likes)
        int commentLikesEngagement = 0;
        if (user.getComments() != null) {
            commentLikesEngagement = user.getComments().stream()
                    .mapToInt(Comment::getLikes) // מניח של-Comment יש getLikes()
                    .sum();
        }

        // 4. ⭐️ מעורבות מעוקבים (Followers)
        // יש לבחור משקל לכמות העוקבים. נניח שכל עוקב שווה נקודה אחת.
        int followersEngagement = 0;
        // מניח שלמשתמש יש שדה המייצג את מספר העוקבים, לדוגמה: getFollowersCount()
        // אם השדה הוא רשימה של עוקבים, יש להשתמש ב: user.getFollowers().size()
        if (user.getFollowerCount() > 0) {
            followersEngagement = user.getFollowerCount();
        }

        return postEngagement + sheetMusicEngagement + commentLikesEngagement + followersEngagement;
    }




    private static double convertEngagementToStarRating(int totalEngagement) {
        final int MAX_ENGAGEMENT_FOR_5_STARS = 500;

        if (totalEngagement >= MAX_ENGAGEMENT_FOR_5_STARS) {
            return 5.0;
        }

        double rawRating = (double)totalEngagement / (MAX_ENGAGEMENT_FOR_5_STARS / 5.0);

        return Math.min(rawRating, 5.0);
    }
}

