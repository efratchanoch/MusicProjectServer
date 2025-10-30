package com.example.tunehub.service;

import com.example.tunehub.model.SheetMusic;
import com.example.tunehub.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SheetMusicRepository  extends JpaRepository<SheetMusic, Long> {
    SheetMusic findSheetMusicById(Long id);
}
