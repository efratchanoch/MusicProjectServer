package com.example.tunehub.dto;

public class UsersSignUpDTO {
    private String name;
    private String email;
    private String password;
    private String imageProfilePath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageProfilePath() {
        return imageProfilePath;
    }

    public void setImageProfilePath(String imageProfilePath) {
        this.imageProfilePath = imageProfilePath;
    }
}
