package com.example.tunehub.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity

public class Teacher extends Users {

    private double pricePerLesson;

    private int experience;

    private double lessonDuration;

    private double rating;

    @OneToMany(mappedBy = "teacher")
    private List<Users> students;

    private LocalDate dateUploaded;

    @ManyToMany
    private List<Instrument> instruments;

    public Teacher() {
    }

    public Teacher(Long id, String name, String password, String email, String description, EUserType EUserType, LocalDate createdAt, LocalDate editedIn, boolean isActive, Date lastActivityTimestamp, String city, String country, List<Instrument> instrumentsUsers, Teacher teacher, String imageProfilePath, List<SheetMusic> sheetsMusic, List<Post> posts, List<Comment> comments, Set<Post> mentionedInPosts, List<Notification> receivedNotifications, List<Notification> sentNotifications, int followerCount, Set<Role> roles, double pricePerLesson, int experience, double lessonDuration, double rating, List<Users> students, LocalDate dateUploaded, List<Instrument> instruments) {
        super(id, name, password, email, description, EUserType, createdAt, editedIn, isActive, lastActivityTimestamp, city, country, instrumentsUsers, teacher, imageProfilePath, sheetsMusic, posts, comments, mentionedInPosts, receivedNotifications, sentNotifications, followerCount, roles);
        this.pricePerLesson = pricePerLesson;
        this.experience = experience;
        this.lessonDuration = lessonDuration;
        this.rating = rating;
        this.students = students;
        this.dateUploaded = dateUploaded;
        this.instruments = instruments;
    }

    public double getPricePerLesson() {
        return pricePerLesson;
    }

    public void setPricePerLesson(double pricePerLesson) {
        this.pricePerLesson = pricePerLesson;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public double getLessonDuration() {
        return lessonDuration;
    }

    public void setLessonDuration(double lessonDuration) {
        this.lessonDuration = lessonDuration;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<Users> getStudents() {
        return students;
    }

    public void setStudents(List<Users> students) {
        this.students = students;
    }

    public LocalDate getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(LocalDate dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<Instrument> instruments) {
        this.instruments = instruments;
    }
}
