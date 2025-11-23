package com.example.tunehub.dto;

import java.time.Instant;

public record NotificationResponseDTO(
        Long id,
        String title,
        String message,
        Instant createdAt,
        boolean isRead,
        Long targetId,
        UsersProfileDTO actor
) {}
