package com.example.tunehub.dto;

import com.example.tunehub.model.EDifficultyLevel;
import com.example.tunehub.model.EScale;

import java.time.LocalDate;
import java.util.List;

public record SheetMusicResponseDTO (
        Long id,
        String name,
        List<InstrumentResponseDTO> instruments, // אם יש DTO תגובה נפרד
        List<SheetMusicCategoryResponseDTO> categories, // אם יש DTO תגובה נפרד
        EDifficultyLevel level,
        EScale scale,
        String filePath,
        UsersProfileDTO user, // רק מזהה המשתמש המעלה
        LocalDate dateUploaded, // תאריך העלאה
        int downloads,
        int pages,
        String imageCoverName,
        double rating,
        boolean isLiked,
        boolean isFavorite,

        int hearts,
        int likes

){}

