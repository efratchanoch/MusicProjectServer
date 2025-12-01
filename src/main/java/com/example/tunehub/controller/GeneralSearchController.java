package com.example.tunehub.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.tunehub.dto.GlobalSearchResponseDTO;
import com.example.tunehub.service.GeneralSearchService;

@RestController
@RequestMapping("/api/search")
public class GeneralSearchController {

    private GeneralSearchService generalSearchService;

    @Autowired
    public GeneralSearchController(GeneralSearchService generalSearchService) {
        this.generalSearchService = generalSearchService;
    }

    @GetMapping("/global/{searchTerm}")
    public ResponseEntity<GlobalSearchResponseDTO> globalSearch(@PathVariable String searchTerm) {
        try {
            GlobalSearchResponseDTO results = generalSearchService.executeGlobalSearch(searchTerm);

            if (!results.hasResults()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(results, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}