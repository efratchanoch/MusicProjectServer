package com.example.tunehub.dto;

import java.time.LocalDate;

public record CommentDTO(
        Long id,
        String content,
        LocalDate dateUploaded,
        UsersProfileDTO profile,
        int likes,
        int hearts,
        boolean isLiked
) {
}