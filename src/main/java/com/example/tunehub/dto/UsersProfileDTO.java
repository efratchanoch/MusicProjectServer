package com.example.tunehub.dto;

import com.example.tunehub.model.Role;

import java.util.HashSet;
import java.util.Set;

public class UsersProfileDTO {
        private String id;
        private String name;
        private String imageProfilePath;
        private Set<RoleDTO> roles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageProfilePath() {
        return imageProfilePath;
    }

    public void setImageProfilePath(String imageProfilePath) {
        this.imageProfilePath = imageProfilePath;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }
}
