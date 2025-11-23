package com.example.tunehub.controller;

import com.example.tunehub.service.NotificationMapper;
import com.example.tunehub.service.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.tunehub.dto.NotificationResponseDTO;
import com.example.tunehub.model.Notification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.AccessDeniedException;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    //private final SimpMessagingTemplate messaging;

    @Autowired
    public NotificationController(NotificationRepository notificationRepository, NotificationMapper notificationMapper)
    //, SimpMessagingTemplate messaging)
    {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        //this.messaging = messaging;
    }

    // Get
    @GetMapping("/notifications")
    public ResponseEntity<Page<NotificationResponseDTO>> getNotificationsByUserId(@RequestParam Long userId) {
        try {
            Page<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE));

            Page<NotificationResponseDTO> dtoPage = notifications.map(n -> notificationMapper.NotificationToNotificationResponseDTO(n));

            return new ResponseEntity<>(dtoPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/unreadCount")
    public ResponseEntity<Long> unreadCountByUserId(@RequestParam Long userId) {
        try {
            Long count = notificationRepository.countByUserIdAndIsReadFalse(userId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Post
    @PostMapping("/createNotification")
    public ResponseEntity<NotificationResponseDTO> createNotification(@RequestBody Notification n) {
        try {
            Notification saved = notificationRepository.save(n);
            NotificationResponseDTO dto = notificationMapper.NotificationToNotificationResponseDTO(saved);
        //    messaging.convertAndSend("/topic/notifications/" + saved.getUser().getId(), dto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @PostMapping("/read/{id}")
    public ResponseEntity<?> markRead(@PathVariable Long id, @RequestParam Long userId) {
        try {
            Notification n = notificationRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

            if (!n.getUser().getId().equals(userId))
                throw new AccessDeniedException("No access to this notification!");

            n.setRead(true);
            notificationRepository.save(n);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/allRead")
    @Transactional
    public ResponseEntity<?> markAllReadByUserId(@RequestParam Long userId) {
        try {
            notificationRepository.markAllAsRead(userId);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


