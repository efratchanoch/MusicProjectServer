package com.example.tunehub.service;

import com.example.tunehub.model.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // קבלת התראות לפי המשתמש
    Page<Notification> findByUser_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    // ספירת התראות שלא נקראו
    long countByUser_IdAndIsReadFalse(Long userId);

    // סימון כל ההתראות כנקראו

    @Modifying(clearAutomatically = true, flushAutomatically = true)  // 🚨 הוסף flushAutomatically    @Transactional // <--- ודא שזה קיים כאן גם// <--- הוסף את הפרמטר הזה!
    @Query("update Notification n set n.isRead = true where n.user.id = :userId and n.isRead = false")
    int markAllAsRead(@Param("userId") Long userId);


    // קבלת התראה מסוימת ששייכת למשתמש (למניעת גישה זרה)
    Optional<Notification> findByIdAndUser_Id(Long id, Long userId);

    // מחיקת התראה ששייכת למשתמש
    void deleteByIdAndUser_Id(Long id, Long userId);


    @Query("SELECT n FROM Notification n " +
            "WHERE n.user = :user " +
            "AND (:#{#categoryTypes == null} = true OR n.type IN :categoryTypes) " + // *** ודא שאתה משתמש ב-IN :categoryTypes ***
            "AND (:readStatus IS NULL OR n.isRead = :readStatus) " +
            "ORDER BY n.createdAt DESC")
    Page<Notification> findFilteredNotifications(
            @Param("user") Users user,
            @Param("categoryTypes") java.util.Collection<ENotificationType> categoryTypes, // שינוי הפרמטר!
            @Param("readStatus") Boolean readStatus,
            Pageable pageable
    );
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.type = :type AND n.isRead = false")
    Long countUnreadByUserIdAndType(
            @Param("userId") Long userId,
            @Param("type") ENotificationType type
    );

    Optional<Notification> findByTypeAndUserAndTargetTypeAndTargetId(ENotificationType notifType, Users user, ETargetType targetType, Long targetId);

   
    Page<Notification> findByUser(Users currentUser, boolean read, Pageable pageable);

    Page<Notification> findByUserAndCategory(Users currentUser, ENotificationCategory category, boolean read, Pageable pageable);


}
