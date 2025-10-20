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
    private long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<SheetMusic> sheetMusic;
}
