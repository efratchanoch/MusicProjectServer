package com.example.tunehub.controller;

import com.example.tunehub.model.ENotificationCategory;
import com.example.tunehub.model.Users;
import com.example.tunehub.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.tunehub.dto.NotificationResponseDTO;

import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/notification")
//@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public Page<NotificationResponseDTO> getNotifications(@PageableDefault(size = 20) Pageable pageable) {
        return notificationService.getNotifications(pageable);
    }

    @GetMapping("/unread-count")
    public long unreadCount() {
        return notificationService.getUnreadCount();
    }

    @PostMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }

    @PostMapping("/read-all")
    public void markAllAsRead() {
        notificationService.markAllAsRead();

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        notificationService.deleteNotification(id);
    }

    @GetMapping("/unreadByType")
    public ResponseEntity<Map<String, Long>> getUnreadByCategory(
           // @AuthenticationPrincipal Users user
    ) {
        Map<String, Long> map = notificationService.getUnreadCountByType();
        return ResponseEntity.ok(map);
    }

    @GetMapping("/allNotificationsByCategory")
    public Page<NotificationResponseDTO> getAllNotificationsByCategory(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) ENotificationCategory category
    ) {
        return notificationService.getAllNotificationsByCategory(category,pageable);
    }

    @GetMapping("/onlyUnreadNotificationsByCategory")
    public Page<NotificationResponseDTO> getOnlyUnreadNotificationsByCategory(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) ENotificationCategory category
    ) {
        return notificationService.getOnlyUnreadNotificationsByCategory(category,pageable);
    }



}
