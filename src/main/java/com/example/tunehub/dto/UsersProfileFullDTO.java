package com.example.tunehub.dto;
import com.example.tunehub.model.*;
import java.time.LocalDate;
import java.util.List;

public class UsersProfileFullDTO {
    private String name;

    private String email;

    private String description;

    private EUserType EUserType;

    private LocalDate createdAt;

    private LocalDate editedIn;

    private boolean isActive;

    private String city;

    private String country;

    private List<UsersProfileDTO> followers;

    private List<UsersProfileDTO> following;

    private List<InstrumentResponseDTO> instrumentsUsers;

    private UsersProfileDTO teacher;

    private String imageProfilePath;


    private List<SheetMusicResponseDTO> sheetsMusic;


    private List<PostResponseDTO> posts;


    //  private List<Comment> comments;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EUserType getEUserType() {
        return EUserType;
    }

    public void setEUserType(EUserType EUserType) {
        this.EUserType = EUserType;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getEditedIn() {
        return editedIn;
    }

    public void setEditedIn(LocalDate editedIn) {
        this.editedIn = editedIn;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public List<UsersProfileDTO> getFollowers() {
        return followers;
    }

    public void setFollowers(List<UsersProfileDTO> followers) {
        this.followers = followers;
    }

    public List<UsersProfileDTO> getFollowing() {
        return following;
    }

    public void setFollowing(List<UsersProfileDTO> following) {
        this.following = following;
    }

    public List<InstrumentResponseDTO> getInstrumentsUsers() {
        return instrumentsUsers;
    }

    public void setInstrumentsUsers(List<InstrumentResponseDTO> instrumentsUsers) {
        this.instrumentsUsers = instrumentsUsers;
    }

    public UsersProfileDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(UsersProfileDTO teacher) {
        this.teacher = teacher;
    }

    public String getImageProfilePath() {
        return imageProfilePath;
    }

    public void setImageProfilePath(String imageProfilePath) {
        this.imageProfilePath = imageProfilePath;
    }

    public List<SheetMusicResponseDTO> getSheetsMusic() {
        return sheetsMusic;
    }

    public void setSheetsMusic(List<SheetMusicResponseDTO> sheetsMusic) {
        this.sheetsMusic = sheetsMusic;
    }

    public List<PostResponseDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponseDTO> posts) {
        this.posts = posts;
    }
}
