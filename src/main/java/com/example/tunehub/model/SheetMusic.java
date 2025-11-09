package com.example.tunehub.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class SheetMusic {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private EScale scale;

    private int likes = 0;

    private String filePath;

    private int hearts = 0;

    private LocalDate dateUploaded;

    @Enumerated(EnumType.STRING)
    private EDifficultyLevel level;

    @ManyToOne
    private Users user;

    @ManyToMany
    private List<Instrument> instruments;

    @ManyToOne
    private SheetMusicCategory category;

    @ManyToMany
    private List<Users> usersFavorite;


    public SheetMusic() {
    }

    public SheetMusic(Long id, String name, EScale scale, int likes, String filePath, int hearts, LocalDate dateUploaded, EDifficultyLevel level, Users user, List<Instrument> instruments, SheetMusicCategory category, List<Users> usersFavorite) {
        this.id = id;
        this.name = name;
        this.scale = scale;
        this.likes = likes;
        this.filePath = filePath;
        this.hearts = hearts;
        this.dateUploaded = dateUploaded;
        this.level = level;
        this.user = user;
        this.instruments = instruments;
        this.category = category;
        this.usersFavorite = usersFavorite;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<Users> getUsersFavorite() {
        return usersFavorite;
    }

    public void setUsersFavorite(List<Users> usersFavorite) {
        this.usersFavorite = usersFavorite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EScale getScale() {
        return scale;
    }

    public void setScale(EScale scale) {
        this.scale = scale;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public LocalDate getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(LocalDate dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public EDifficultyLevel getLevel() {
        return level;
    }

    public void setLevel(EDifficultyLevel level) {
        this.level = level;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<Instrument> instruments) {
        this.instruments = instruments;
    }

    public SheetMusicCategory getCategory() {
        return category;
    }

    public void setCategory(SheetMusicCategory category) {
        this.category = category;
    }
}
