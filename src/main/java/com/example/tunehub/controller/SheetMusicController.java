package com.example.tunehub.controller;

import com.example.tunehub.model.*;
import com.example.tunehub.service.SheetMusicMapper;
import com.example.tunehub.service.SheetMusicRepository;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    //Get
    @GetMapping("/sheetMusicById/{id}")
    public ResponseEntity<SheetMusic> getSheetMusicById(@PathVariable Long id) {
        try {
            SheetMusic s = sheetMusicRepository.findSheetMusicById(id);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusic")
    public ResponseEntity<List<SheetMusic>> getSheetsMusic() {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAll();
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/sheetsMusicByUserId/{id}")
    public ResponseEntity<List<SheetMusic>> getSheetsMusicByUserId(@PathVariable Long id) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByUser_Id(id);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/favoriteSheetsMusicByUserId/{id}")
    public ResponseEntity<List<SheetMusic>> getFavoriteSheetsMusicByUserId(@PathVariable Long id) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByUsersFavorite_Id(id);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByName/{name}")
    public ResponseEntity<List<SheetMusic>> getSheetsMusicByName(String name) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllByName(name) ;
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByCategory/{category_id}")
    public ResponseEntity<List<SheetMusic>> getSheetsMusicByCategory(@PathVariable Long category_id) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByCategory_Id(category_id) ;
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByScale/{scale}")
    public ResponseEntity<List<SheetMusic>> getSheetsMusicByScale(@PathVariable EScale scale) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByScale(scale) ;
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByLevel/{level}")
    public ResponseEntity<List<SheetMusic>> getSheetsMusicByLevel(@PathVariable EDifficultyLevel level) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByLevel(level) ;
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
