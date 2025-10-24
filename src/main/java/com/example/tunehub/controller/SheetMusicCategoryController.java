package com.example.tunehub.controller;

import com.example.tunehub.service.SheetMusicCategoryMapper;
import com.example.tunehub.service.SheetMusicCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sheetMusicCategory")
@CrossOrigin
public class SheetMusicCategoryController {
    private SheetMusicCategoryRepository sheetMusicCategoryRepository;
    private SheetMusicCategoryMapper sheetMusicCategoryMapper;

    @Autowired
    public SheetMusicCategoryController(SheetMusicCategoryRepository sheetMusicCategoryRepository, SheetMusicCategoryMapper sheetMusicCategoryMapper) {
        this.sheetMusicCategoryRepository = sheetMusicCategoryRepository;
        this.sheetMusicCategoryMapper = sheetMusicCategoryMapper;
    }
}
