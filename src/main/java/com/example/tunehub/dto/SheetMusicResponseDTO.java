package com.example.tunehub.dto;

import com.example.tunehub.model.EDifficultyLevel;
import com.example.tunehub.model.EScale;

import java.time.LocalDate;
import java.util.List;

public class SheetMusicResponseDTO {
    private Long id; // המזהה הייחודי
    private String name;
    private List<InstrumentResponseDTO> instruments; // אם יש DTO תגובה נפרד
    private SheetMusicCategoryResponseDTO category; // אם יש DTO תגובה נפרד
    private EDifficultyLevel level;
    private EScale scale;
    private String filePath;
    private UsersProfileDTO user; // רק מזהה המשתמש המעלה
    private LocalDate dateUploaded; // תאריך העלאה
    private int downloads;
    private int pages;

    private boolean isLiked;
    private boolean isFavorite;

    private int hearts;
    private int likes;

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

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
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

    public List<InstrumentResponseDTO> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<InstrumentResponseDTO> instruments) {
        this.instruments = instruments;
    }

    public SheetMusicCategoryResponseDTO getCategory() {
        return category;
    }

    public void setCategory(SheetMusicCategoryResponseDTO category) {
        this.category = category;
    }

    public EDifficultyLevel getLevel() {
        return level;
    }

    public void setLevel(EDifficultyLevel level) {
        this.level = level;
    }

    public EScale getScale() {
        return scale;
    }

    public void setScale(EScale scale) {
        this.scale = scale;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public UsersProfileDTO getUser() {
        return user;
    }

    public void setUser(UsersProfileDTO user) {
        this.user = user;
    }

    public LocalDate getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(LocalDate dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
