package com.example.tunehub.dto;

import com.example.tunehub.model.EUserType;

import java.time.LocalDate;

public record UsersMusiciansDTO (
         UsersProfileDTO profile,
         String city,
         String country,
         boolean isActive,
         String description,
         InstrumentResponseDTO instruments,
         EUserType EUserType,
         LocalDate createDate
){}

