package com.example.tunehub.dto;

import java.time.LocalDate;
import java.util.Date;

public record SheetMusicSearchDTO (
        Long id,
        String title,
        String userName,
        LocalDate dateUploaded,
        String imageCoverName
){ }

