package com.example.tunehub.dto;

import java.time.LocalDate;

public record PostSearchDTO(
        Long id,
        String title,
        String userName,
        LocalDate dateUploaded
) {
}
