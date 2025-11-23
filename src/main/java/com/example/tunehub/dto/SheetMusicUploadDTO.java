package com.example.tunehub.dto;

import com.example.tunehub.model.EDifficultyLevel;
import com.example.tunehub.model.EScale;

import java.util.List;

public record SheetMusicUploadDTO(
        String title,
        List<InstrumentResponseDTO> instruments,
        List<SheetMusicCategoryResponseDTO> categories,
        EDifficultyLevel level,
        EScale scale,
        String fileName,
        UsersDTO user,
        String composer,
        String lyricist
) {}
