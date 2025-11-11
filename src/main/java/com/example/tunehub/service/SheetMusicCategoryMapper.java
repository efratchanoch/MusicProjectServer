package com.example.tunehub.service;

import com.example.tunehub.dto.SheetMusicCategoryResponseDTO;
import com.example.tunehub.model.SheetMusicCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SheetMusicCategoryMapper {
    @Named("toSheetMusicCategory")
    SheetMusicCategory sheetMusicCategoryDTOtoSheetMusicCategory(SheetMusicCategoryResponseDTO sheetMusicCategoryDTO);

    SheetMusicCategoryResponseDTO  sheetMusicCategoryToSheetMusicCategoryDTO(SheetMusicCategory sheetMusicCategoryDTO);

    List<SheetMusicCategoryResponseDTO> sheetMusicCategoryListToSheetMusicCategoryDTOList(List<SheetMusicCategory> sheetMusicCategory);
}
