package com.example.tunehub.dto;

import java.util.List;

public class PostCreateDTO {

    private String title;
    private String content;
    // נתיבים/URLs של קבצי המדיה שכבר הועלו לשירות אחסון
    private List<String> mediaUris;

    public PostCreateDTO() {
    }

    public PostCreateDTO(String title, String content, List<String> mediaUris) {
        this.title = title;
        this.content = content;
        this.mediaUris = mediaUris;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getMediaUris() {
        return mediaUris;
    }

    public void setMediaUris(List<String> mediaUris) {
        this.mediaUris = mediaUris;
    }
}

