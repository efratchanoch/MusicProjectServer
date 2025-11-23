package com.example.tunehub.service;

import com.example.tunehub.dto.InstrumentResponseDTO;
import com.example.tunehub.dto.SheetMusicCategoryResponseDTO;
import com.example.tunehub.model.Instrument;
import com.example.tunehub.model.SheetMusicCategory;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SheetMusicCategoryMapper {
    @Named("toSheetsMusicCategory")
    List<SheetMusicCategory> sheetMusicCategoryDTOtoSheetMusicCategory(List<SheetMusicCategoryResponseDTO> sheetMusicCategoryDTO);

    SheetMusicCategoryResponseDTO sheetMusicCategoryToSheetMusicCategoryDTO(SheetMusicCategory sheetMusicCategoryDTO);

    List<SheetMusicCategoryResponseDTO> sheetMusicCategoryListToSheetMusicCategoryDTOList(List<SheetMusicCategory> sheetMusicCategory);

    List<SheetMusicCategory> sheetMusicCategoryResponseDTOListToSheetMusicCategoryList(List<SheetMusicCategoryResponseDTO> s);

    @Named("mapCategoryListWithoutCreate")
    @IterableMapping(qualifiedByName = "mapCategory")
    List<SheetMusicCategory> mapInstrumentListWithoutCreate(List<SheetMusicCategoryResponseDTO> list);

    @Named("mapCategory")
    SheetMusicCategory mapCategory(SheetMusicCategoryResponseDTO c);

}
