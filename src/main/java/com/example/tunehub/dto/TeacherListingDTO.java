package com.example.tunehub.dto;

import java.util.List;

public class TeacherListingDTO {
    private Long id;
    private String name;
    private String city;
    private String country;
    private double pricePerLesson;
    private int lessonDuration;
    private int experience;

    private List<InstrumentDTO> instruments; // נשתמש ב-DTO נוסף לכלי נגינה



    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getLessonDuration() {
        return lessonDuration;
    }

    public void setLessonDuration(int lessonDuration) {
        this.lessonDuration = lessonDuration;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public double getPricePerLesson() {
        return pricePerLesson;
    }

    public void setPricePerLesson(double pricePerLesson) {
        this.pricePerLesson = pricePerLesson;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public TeacherListingDTO(Long id, String name, String city, String country, double pricePerLesson, int lessonDuration, int experience, List<InstrumentDTO> instruments) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.country = country;
        this.pricePerLesson = pricePerLesson;
        this.lessonDuration = lessonDuration;
        this.experience = experience;
        this.instruments = instruments;
    }

    public List<InstrumentDTO> getInstruments() { return instruments; }
    public void setInstruments(List<InstrumentDTO> instruments) { this.instruments = instruments; }
}



