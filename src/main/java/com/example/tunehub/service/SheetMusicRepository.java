package com.example.tunehub.service;
import com.example.tunehub.model.SheetMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SheetMusicRepository  extends JpaRepository<SheetMusic, Long> {
    SheetMusic findSheetMusicById(Long id);

    List<SheetMusic> findAllSheetMusicByUser_Id(Long id);

    List<SheetMusic> findAllSheetMusicByUsersFavorite_Id(Long id);
}
