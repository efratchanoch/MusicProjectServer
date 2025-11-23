package com.example.tunehub.service;
import com.example.tunehub.model.EDifficultyLevel;
import com.example.tunehub.model.EScale;
import com.example.tunehub.model.SheetMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SheetMusicRepository  extends JpaRepository<SheetMusic, Long> {
    List<SheetMusic> findAllByUserId(Long id);

    SheetMusic findSheetMusicById(Long id);

    List<SheetMusic> findAllSheetMusicByUser_Id(Long id);

    //List<SheetMusic> findAllSheetMusicByUsersFavorite_Id(Long id);

    List<SheetMusic> findAllByTitle(String title);

    //List<SheetMusic> findAllSheetMusicByCategory_Id(Long categoryId);
    List<SheetMusic> findAllByCategories_Id(Long categoryId);
    List<SheetMusic> findAllSheetMusicByScale(EScale scale);

    List<SheetMusic> findAllSheetMusicByLevel(EDifficultyLevel level);

    void deleteAllByUserId(Long id);

    // הדרך האוטומטית ל־ManyToMany
    @Modifying
    @Query("""
    delete from SheetMusic s
    where exists (
        select 1 from s.categories c
        where c.id = :categoryId
    )
""")
    void deleteAllByCategoryId(@Param("categoryId") Long categoryId);





    @Modifying
    @Query("UPDATE SheetMusic sm SET sm.likes = :count WHERE sm.id = :id")
    @Transactional
    void updateLikeCount(@Param("id") Long sheetMusicId, @Param("count") int count);

}
