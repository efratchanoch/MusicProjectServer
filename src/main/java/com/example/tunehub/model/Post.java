package com.example.tunehub.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Users user;

    private String title;

    private String content;

    private int hearts = 0;

    private int likes = 0;

    private String audio;

    private String video;

    private LocalDate dateUploaded;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    private List<String> imagesPath;

    private List<String> audiosPath;

    @ManyToMany
    private List<Users> usersFavorite;

    public Post() {
    }

    public Post(Long id, Users user, String title, String content, int hearts, int likes, String audio, String video, LocalDate dateUploaded, List<Comment> comments, List<String> imagesPath, List<String> audiosPath, List<Users> usersFavorite) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.hearts = hearts;
        this.likes = likes;
        this.audio = audio;
        this.video = video;
        this.dateUploaded = dateUploaded;
        this.comments = comments;
        this.imagesPath = imagesPath;
        this.audiosPath = audiosPath;
        this.usersFavorite = usersFavorite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(List<String> imagesPath) {
        this.imagesPath = imagesPath;
    }

    public List<String> getAudiosPath() {
        return audiosPath;
    }

    public void setAudiosPath(List<String> audiosPath) {
        this.audiosPath = audiosPath;
    }

    public List<Users> getUsersFavorite() {
        return usersFavorite;
    }

    public void setUsersFavorite(List<Users> usersFavorite) {
        this.usersFavorite = usersFavorite;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Long getPost_id() {
        return id;
    }

    public void setPost_id(Long post_id) {
        this.id = post_id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        content = content;
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

    public LocalDate getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(LocalDate dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


}

