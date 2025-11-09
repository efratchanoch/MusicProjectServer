package com.example.tunehub.service;

import com.example.tunehub.dto.SheetMusicCategoryDTO;
import com.example.tunehub.model.SheetMusicCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SheetMusicCategoryMapper {
    SheetMusicCategory sheetMusicCategoryDTOtoSheetMusicCategory(SheetMusicCategoryDTO sheetMusicCategoryDTO);
}
