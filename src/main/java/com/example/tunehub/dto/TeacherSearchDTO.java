package com.example.tunehub.dto;

public record TeacherSearchDTO(
        UsersProfileDTO profile,
        String country,
        String city
) {
}
