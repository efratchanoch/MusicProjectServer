package com.example.tunehub.service;

import com.example.tunehub.dto.NotificationFollowDTO;
import com.example.tunehub.dto.NotificationLikesAndFavoritesDTO;
import com.example.tunehub.dto.NotificationResponseDTO;
import com.example.tunehub.dto.NotificationSimpleDTO;
import com.example.tunehub.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service

public class NotificationService {


    private final NotificationRepository notificationRepository;
    private final UsersRepository usersRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AuthService authService;
    private final NotificationMapper notificationMapper;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UsersRepository usersRepository, SimpMessagingTemplate messagingTemplate, AuthService authService, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.usersRepository = usersRepository;
        this.messagingTemplate = messagingTemplate;
        this.authService = authService;
        this.notificationMapper = notificationMapper;
    }


    private ENotificationType getNotificationTypeByTargetTypeAndInteractionType(ETargetType targetType, String interactionType) {
        switch (targetType) {
            case POST:
                if (interactionType.equals("LIKE")) {
                    return ENotificationType.LIKE_POST;
                } else {
                    return ENotificationType.FAVORITE_POST;
                }

            case SHEET_MUSIC:
                if (interactionType.equals("LIKE")) {
                    return ENotificationType.LIKE_MUSIC;
                } else {
                    return ENotificationType.FAVORITE_MUSIC;
                }

            case COMMENT:
                return ENotificationType.LIKE_COMMENT;

            default:
                throw new IllegalArgumentException("Unsupported ETargetType: " + targetType);
        }
    }

    /**
     * צור התראה ושדר אותה בזמן אמת ל-user channel (לפי userId).
     * targetUserId = המיועד לקבל את ההתראה.
     * sender = מי היזם (נלקח מה־AuthService).
     */
    @Transactional
    public void createAndSendNotification(Long targetUserId,
                                          ENotificationType type,
                                          String title,
                                          String message,
                                          Long targetEntityId) {

        Users sender = authService.getCurrentUser();

        Users target = usersRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        Notification n = new Notification();
        n.setActor(sender);
        n.setUser(target);
        n.setType(type);
        n.setTitle(title);
        n.setMessage(message);
        n.setTargetType(null);
        n.setTargetId(targetEntityId);
        n.setRead(false);
        n.setCreatedAt(Instant.now());

        notificationRepository.save(n);

        NotificationResponseDTO dto = notificationMapper.NotificationToNotificationResponseDTO(n);

        // שליחה לפי USER ID (הגיוני וברי-קיימא)
        messagingTemplate.convertAndSendToUser(
                String.valueOf(target.getId()),
                "/queue/notifications",
                dto
        );
    }


    public long getUnreadCount() {
        Long userId = authService.getCurrentUserId();
        return notificationRepository.countByUser_IdAndIsReadFalse(userId);

//        messagingTemplate.convertAndSendToUser(
//                String.valueOf(userId),
//                "/queue/notifications/count-unread",
//                n.getId()
//        );
    }

    @Transactional
    public void markAsRead(Long id) {
        Long userId = authService.getCurrentUserId();

        Notification n = notificationRepository.findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        n.setRead(true);
        notificationRepository.save(n);

        messagingTemplate.convertAndSendToUser(
                String.valueOf(userId),
                "/queue/notifications/read",
                n.getId()
        );
    }


    @Transactional
    public void markAllAsRead() {
        Long userId = authService.getCurrentUserId();
        notificationRepository.markAllAsRead(userId);
        messagingTemplate.convertAndSendToUser(
                String.valueOf(userId),
                "/queue/notifications/mark-all-read",
                "OK"
        );
    }

    @Transactional
    public void deleteNotification(Long id) {
        Long userId = authService.getCurrentUserId();
        notificationRepository.deleteByIdAndUser_Id(id, userId);
    }

    @Transactional
    public void saveNotification(Notification notification) {

        notificationRepository.save(notification);

        NotificationResponseDTO dto =
                notificationMapper.NotificationToNotificationResponseDTO(notification);

        messagingTemplate.convertAndSendToUser(
                String.valueOf(notification.getUser().getId()),
                "/queue/notifications",
                dto
        );
    }


    @Transactional
    public void createNotification(ENotificationType type,
                                   Users target,
                                   Users actor,
                                   ETargetType targetType,
                                   Long targetEntityId) {

        Notification n = new Notification();
        n.setType(type);
        n.setUser(target);
        n.setActor(actor);
        n.setTargetType(targetType);
        n.setTargetId(targetEntityId);
        n.setRead(false);
        n.setCreatedAt(Instant.now());

        // ברירת מחדל (תמיד קיים)
        n.setTitle(type.name());
        n.setMessage(null);

        notificationRepository.save(n);

        NotificationResponseDTO dto = notificationMapper.NotificationToNotificationResponseDTO(n);

        messagingTemplate.convertAndSendToUser(
                String.valueOf(target.getId()),
                "/queue/notifications",
                dto
        );
    }

    public Map<String, Long> getUnreadCountByType() {
        Long userId = authService.getCurrentUserId();
        Map<String, Long> result = new HashMap<>();

        for (ENotificationType type : ENotificationType.values()) {
            Long count = notificationRepository.countUnreadByUserIdAndType(userId, type);
            result.put(type.name(), count);
        }

        return result;
    }


    //מטפל בהוספה ומחיקת לייקים - שולחהתראה לבעלים
    public void handleLikeNotification(
            ETargetType targetType,
            Long targetId,
            Users contentOwner,
            int newCount
    ) {

        //מקבלים את הסוג מסג שאמור להיות
        ENotificationType notifType =
                getNotificationTypeByTargetTypeAndInteractionType(targetType, "LIKE");

        //בודקים אם יש לו כבר התראה בנושא
        Optional<Notification> existing = notificationRepository
                .findByTypeAndUserAndTargetTypeAndTargetId(
                        notifType,
                        contentOwner,
                        targetType,
                        targetId
                );
        Notification n;
// אם הוא קיים נרצה לתפוס אותו
        if (existing.isPresent()) {
            n = existing.get();
            //נסמן את ההתראה כלא נקראה
            n.setRead(false);
        } else {
            // אם הוא לא קיים ניצור אותו
            n = new Notification(
                    notifType,
                    contentOwner,
                    null, //לא מעניין מי עשה את הלייק אין לנו עניין לשמור מי עשה
                    targetType,
                    targetId
            );
        }
        //מעברים לDTO החזרה מסוג ליק אנד פבורייט
        NotificationLikesAndFavoritesDTO dto = notificationMapper.NotificationToNotificationLikesAndFavoritesDTO(n);
        //מעדכנים כאונט ותאריך ושעה
        dto.setCount(newCount);
        dto.setDateTime(LocalDateTime.now());
        notificationRepository.save(n);

        messagingTemplate.convertAndSendToUser(
                contentOwner.getName(),
                "/queue/notifications",
                dto
        );
    }


    public void handleUnfollowNotification(Users follower, Users contentOwner) {

        // יוצרים את ההתראה
        Notification n = new Notification(
                ENotificationType.FOLLOWER_REMOVED,
                contentOwner,
                follower, // מי שהפסיק לעקוב
                ETargetType.USER, // במקרה של משתמש
                contentOwner.getId() // האיידי של היוזר
        );

        notificationRepository.save(n);

        // לשלוח דרך WebSocket, ממירים ל-DTO
        NotificationFollowDTO dto = notificationMapper.NotificationToNotificationFollowDTO(n);
        messagingTemplate.convertAndSendToUser(
                contentOwner.getName(),
                "/queue/notifications",
                dto
        );

    }


    public void handleFollowRequestNotification(Users follower, Users contentOwner) {

        Notification n = new Notification(
                ENotificationType.FOLLOW_REQUEST_RECEIVED,
                contentOwner,
                follower,
                ETargetType.USER,
                follower.getId()
        );

        notificationRepository.save(n);

        NotificationFollowDTO dto = notificationMapper.NotificationToNotificationFollowDTO(n);
        messagingTemplate.convertAndSendToUser(
                contentOwner.getName(),
                "/queue/notifications",
                dto
        );
    }


    public void handleFollowRequestDecisions(Users approvedUser, Users followedUser, ENotificationType notify) {
        if (approvedUser == null || followedUser == null) {
            return; // לא עושים כלום אם אחד מהם null
        }


// יוצרים את ההתראה
        Notification n = new Notification(
                notify, // סוג ההתראה
                approvedUser,                        // המשתמש שקיבל את ההודעה
                followedUser,                        // מי שאישר   או דחה את העקיבה
                ETargetType.USER,                     // סוג המטרה
                followedUser.getId()                  // מזהה המטרה
        );


        notificationRepository.save(n);

        NotificationFollowDTO dto = notificationMapper.NotificationToNotificationFollowDTO(n);
        messagingTemplate.convertAndSendToUser(
                approvedUser.getName(),
                "/queue/notifications",
                dto
        );

    }

    public Page<NotificationResponseDTO> getNotifications(Pageable pageable) {
        Long userId = authService.getCurrentUserId();

        return notificationRepository
                .findByUser_IdOrderByCreatedAtDesc(userId, pageable)
                .map(notificationMapper::NotificationToNotificationResponseDTO);
    }

    public Page<NotificationResponseDTO> getOnlyUnreadNotificationsByCategory(ENotificationCategory category,
                                                                              Pageable pageable) {
        return getNotifications(pageable, category, false);

    }

    public Page<NotificationResponseDTO> getAllNotificationsByCategory(
            ENotificationCategory category,
            Pageable pageable) {
        return getNotifications(pageable, category, true);

    }


    private Page<NotificationResponseDTO> getNotifications(
            Pageable pageable,
            ENotificationCategory category,
            Boolean read
    ) {


        Users currentUser = authService.getCurrentUser();


        Page<Notification> notifications;
        if (category != null) {
            notifications = notificationRepository
                    .findByUserAndCategory(currentUser, category, read, pageable);
        } else {
            notifications = notificationRepository
                    .findByUser(currentUser, read, pageable);
        }

        // כאן את משתמשת במפה שלך
        return notifications.map(notificationMapper::NotificationToNotificationResponseDTO);


    }

}
