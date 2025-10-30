package com.example.tunehub.dto;

import java.time.LocalDate;

public class CommentDTO {

    private Long id;

    private String content;

    private LocalDate dateUploaded;

    private UsersProfileDTO profile;


    private int likes;

    private int hearts;

    //private UsersUploadProfileImageDTO profile;

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(LocalDate dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public UsersProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(UsersProfileDTO profile) {
        this.profile = profile;
    }
}
