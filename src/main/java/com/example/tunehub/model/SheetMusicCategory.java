package com.example.tunehub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class SheetMusicCategory {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<SheetMusic> sheetMusic;

    public SheetMusicCategory() {
    }

    public SheetMusicCategory(long id, String name, List<SheetMusic> sheetMusic) {
        this.id = id;
        this.name = name;
        this.sheetMusic = sheetMusic;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SheetMusic> getSheetMusic() {
        return sheetMusic;
    }

    public void setSheetMusic(List<SheetMusic> sheetMusic) {
        this.sheetMusic = sheetMusic;
    }
}
