package com.example.tunehub.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity

public class Teacher extends Users {

    private double pricePerLesson;

    private int experience;

    private double lessonDuration;

    private double rating;//תאריך
    //כתובת
    //Dynamic programming

    @OneToMany(mappedBy = "teacher")
    private List<Users> students;

    private LocalDate dateUploaded;

    @ManyToMany
    private List<Instrument> instruments;

    public Teacher() {
    }

    public Teacher(double pricePerLesson, int experience, double lessonDuration, double rating, List<Users> students, LocalDate dateUploaded, List<Instrument> instruments) {
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


}
