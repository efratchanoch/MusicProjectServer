package com.example.tunehub.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;


@Entity
public class Notification {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Users user; // מי מקבל

    @ManyToOne
    private Users actor;    // מי יזם את ההודעה (יכול להיות null)

    @Enumerated(EnumType.STRING)
    private ENotificationType type;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    private ETargetType targetType;

    private Long targetId;

    private boolean isRead = false;

    @CreationTimestamp
    private Instant createdAt;

    public Notification() {
    }

    public Notification(ENotificationType type, Users user, Users actor, ETargetType targetType, Long targetId) {
        this.type = type;
        this.user = user;
        this.actor = actor;
        this.targetType = targetType;
        this.targetId = targetId;
        this.isRead = false; // ברירת מחדל: לא נקרא

        // יצירת Title ו-Message (ראה הסבר בסעיף 2)
        setTitleAndMessageBasedOnType(type, actor);
    }

    private void setTitleAndMessageBasedOnType(ENotificationType type, Users actor) {
        // נניח של-Users יש getName()
        String actorName = (actor != null) ? actor.getName() : "משתמש";

        switch (type) {
            case NEW_LIKE:
                this.title = "לייק חדש!";
                this.message = actorName + " אהב את הפוסט שלך.";
                break;
            case FOLLOW_REQUEST:
                this.title = "בקשת מעקב";
                this.message = actorName + " רוצה לעקוב אחריך.";
                break;
            // ... הוסף לוגיקה לכל ENotificationType ...
            default:
                this.title = "הודעה חדשה";
                this.message = "יש לך הודעה חדשה";
        }
    }

    public Notification(Long id, Users user, Users actor, ENotificationType type, String title, String message, ETargetType targetType, Long targetId, boolean isRead, Instant createdAt) {
        this.id = id;
        this.user = user;
        this.actor = actor;
        this.type = type;
        this.title = title;
        this.message = message;
        this.targetType = targetType;
        this.targetId = targetId;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Users getActor() {
        return actor;
    }

    public void setActor(Users actor) {
        this.actor = actor;
    }

    public ENotificationType getType() {
        return type;
    }

    public void setType(ENotificationType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ETargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(ETargetType targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

