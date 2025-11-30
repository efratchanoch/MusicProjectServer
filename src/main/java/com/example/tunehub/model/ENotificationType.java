package com.example.tunehub.model;

public enum ENotificationType {
    // Likes & Favorites
    LIKE_POST,
    LIKE_COMMENT,
    LIKE_MUSIC,
    FAVORITE_POST,
    FAVORITE_MUSIC,

    // Comments
    COMMENT_ON_POST,

    // Follow Updates (your followees perform actions)
    FOLLOWEE_NEW_POST,
    FOLLOWEE_NEW_MUSIC,
    FOLLOWEE_NEW_PROFILE_PICTURE,
    FOLLOWEE_NEW_VIDEO,
    FOLLOWEE_PROFILE_UPDATED,

    // Approved follows
    FOLLOW_REQUEST_ACCEPTED,
    FOLLOW_REQUEST_DECLINED,

    // Follow requests
    FOLLOW_REQUEST_RECEIVED,
    FOLLOWER_REMOVED,

    // Admin
    ADMIN_WARNING_POST,
    ADMIN_WARNING_COMMENT,
    ADMIN_DELETED_POST,
    ADMIN_DELETED_COMMENT,
    ADMIN_PROMOTION_MANAGER,
    ADMIN_PROMOTION_SUPER_MANAGER
//    SYSTEM_ANNOUNCEMENT,
}
