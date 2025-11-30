package com.example.tunehub.dto;

import java.util.List;

public record PostResponseDTO(
        Long id,
        UsersProfileDTO user,
        String title,
        String content,
        boolean isLiked,
        boolean isFavorite,
        int hearts,
        int likes,
        String audioPath,
        String videoPath,
        List<String> imagesBase64,
        String dateUploaded
) {
}
