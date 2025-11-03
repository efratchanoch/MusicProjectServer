package com.example.tunehub.dto;

import com.example.tunehub.model.EUserType;

import java.time.LocalDate;

public class UsersMusiciansDTO {
    private UsersProfileDTO profile;
    private String city;
    private String country;
    private boolean active;
    private String description;
    private InstrumentsDTO instruments;
    private EUserType EUserType;
    private LocalDate createDate;

//    private String rating;


    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public UsersProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(UsersProfileDTO profile) {
        this.profile = profile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public InstrumentsDTO getInstruments() {
        return instruments;
    }

    public void setInstruments(InstrumentsDTO instruments) {
        this.instruments = instruments;
    }

    public EUserType getUserType() {
        return EUserType;
    }

    public void setUserType(EUserType EUserType) {
        this.EUserType = EUserType;
    }
}
