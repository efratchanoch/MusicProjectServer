package com.example.tunehub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    private int likes = 0;

    @ManyToOne
    private Users user;

    @ManyToOne
    private Post post;

    private String content;

    private LocalDate dateUploaded;

//    private String imageUrl;
//
//    private String audioUrl;

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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

    public Comment(Long id, int likes, Users user, Post post, String content, LocalDate dateUploaded) {
        this.id = id;
        this.likes = likes;
        this.user = user;
        this.post = post;
        this.content = content;
        this.dateUploaded = dateUploaded;
    }
}
