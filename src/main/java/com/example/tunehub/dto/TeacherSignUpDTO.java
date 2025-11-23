package com.example.tunehub.dto;

import java.util.List;

public class TeacherSignUpDTO {

    private double pricePerLesson;
    private int experience;
    private double lessonDuration;
    private List<Long> instrumentsIds; // נשתמש ברשימת ID של כלי נגינה


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

    public List<Long> getInstrumentsIds() {
        return instrumentsIds;
    }

    public void setInstrumentsIds(List<Long> instrumentsIds) {
        this.instrumentsIds = instrumentsIds;
    }
}

