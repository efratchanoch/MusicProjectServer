package com.example.tunehub.controller;

import com.example.tunehub.service.SheetMusicMapper;
import com.example.tunehub.service.SheetMusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sheetMusic")
@CrossOrigin
public class SheetMusicController {
    private SheetMusicRepository sheetMusicRepository;
    private SheetMusicMapper sheetMusicMapper;

    @Autowired
    public SheetMusicController(SheetMusicRepository sheetMusicRepository, SheetMusicMapper sheetMusicMapper) {
        this.sheetMusicRepository = sheetMusicRepository;
        this.sheetMusicMapper = sheetMusicMapper;
    }
}
