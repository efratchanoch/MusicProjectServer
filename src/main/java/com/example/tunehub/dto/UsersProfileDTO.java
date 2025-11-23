package com.example.tunehub.dto;

import java.util.Set;

public record UsersProfileDTO(
        String id,
        String name,
        String imageProfilePath,
        Set<RoleDTO> roles
) {}