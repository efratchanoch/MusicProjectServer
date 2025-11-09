package com.example.tunehub.controller;

import com.example.tunehub.model.Instrument;
import com.example.tunehub.model.Post;
import com.example.tunehub.service.InstrumentMapper;
import com.example.tunehub.service.InstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instrument")
public class InstrumentController {
    private InstrumentRepository instrumentRepository;
    private InstrumentMapper instrumentMapper;

    @Autowired
    public InstrumentController(InstrumentRepository instrumentRepository, InstrumentMapper instrumentMapper) {
        this.instrumentRepository = instrumentRepository;
        this.instrumentMapper = instrumentMapper;
    }

    //Get
    @GetMapping("/instruments")
    public ResponseEntity<List<Instrument>> getInstruments() {
        try {
            List<Instrument> i = instrumentRepository.findAll();
            if (i == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(i, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/instrumentsByUserId/{user_id}")
    public ResponseEntity<List<Instrument>> getInstrumentsByUserId(@PathVariable Long user_id) {
        try {
            List<Instrument> i = instrumentRepository.findAllByUsers_Id(user_id);
            if (i == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(i, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/instrumentsByTeachersId/{teachers_id}")
    public ResponseEntity<List<Instrument>> getInstrumentsByTeachersId(@PathVariable Long teachers_id) {
        try {
            List<Instrument> i = instrumentRepository.findAllByTeachers_Id(teachers_id);
            if (i == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(i, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/instrumentsBySheetMusicId/{sheet_music_id}")
    public ResponseEntity<List<Instrument>> getInstrumentsBySheetMusicId(@PathVariable Long sheet_music_id) {
        try {
            List<Instrument> i = instrumentRepository.findAllBySheetsMusic_Id(sheet_music_id);
            if (i == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(i, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
