package com.example.tunehub.dto;

import com.example.tunehub.model.EDifficultyLevel;
import com.example.tunehub.model.EScale;

import java.util.List;

public class SheetMusicUploadDTO {
    private String name;

    private List<InstrumentResponseDTO> instruments;

    private SheetMusicCategoryResponseDTO category;

    private EDifficultyLevel level;

    private EScale scale;

    private String fileName;

    private UsersDTO user;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getName() {
        return name;
    }

    public UsersDTO getUser() {
        return user;
    }

    public void setUser(UsersDTO user) {
        this.user = user;
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
}
