package com.example.tunehub.dto;

import java.util.List;

public class PostResponseDTO {
    private Long id;
    private Long userId;         // מזהה היוזר שפרסם את הפוסט
    private String title;        // כותרת הפוסט
    private String content;
    private int hearts;          // לבבות
    private int likes;           // לייקים
    private String audioPath;    // שם הקובץ של האודיו (אם קיים)
    private String videoPath;    // שם הקובץ של הווידאו (אם קיים)
    private List<String> imagesBase64; // תמונות בפורמט Base64
    private String dateUploaded; // תאריך העלאה (לדוגמה כמחרוזת)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public List<String> getImagesBase64() {
        return imagesBase64;
    }

    public void setImagesBase64(List<String> imagesBase64) {
        this.imagesBase64 = imagesBase64;
    }


    public String getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(String dateUploaded) {
        this.dateUploaded = dateUploaded;
    }
}
