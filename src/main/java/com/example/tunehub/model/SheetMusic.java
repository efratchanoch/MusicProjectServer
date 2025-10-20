package com.example.tunehub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Entity
public class SheetMusic {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    // private String fileUrl;
    private Scale scale;

    private int likes = 0;

    private int hearts = 0;


    private LocalDate dateUploaded;

    private DifficultyLevel level;

    @ManyToOne
    private Users user;

    @ManyToOne
    private SheetMusicCategory category;

    public SheetMusic() {
    }

    public SheetMusic(Long id, String name, Scale scale, int likes, int hearts, String instruments, LocalDate dateUploaded, DifficultyLevel level, Users user, SheetMusicCategory category) {
        this.id = id;
        this.name = name;
        this.scale = scale;
        this.likes = likes;
        this.hearts = hearts;
//        Instruments = instruments;
        this.dateUploaded = dateUploaded;
        this.level = level;
        this.user = user;
        this.category = category;
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

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
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

    public DifficultyLevel getLevel() {
        return level;
    }

    public void setLevel(DifficultyLevel level) {
        this.level = level;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public SheetMusicCategory getCategory() {
        return category;
    }

    public void setCategory(SheetMusicCategory category) {
        this.category = category;
    }
}
