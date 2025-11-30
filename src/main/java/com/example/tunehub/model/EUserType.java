package com.example.tunehub.model;

public enum EUserType {
    STUDENT , MANAGER , TEACHER, MUSIC_LOVER, MUSICIAN;

    public static EUserType fromValue(int value) {
        return EUserType.values()[value]; // זה עובד אם האינדקסים ב-ENUM הם 0, 1, 2...
    }
}
