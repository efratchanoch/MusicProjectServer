package com.example.tunehub.service;

import com.example.tunehub.dto.SheetMusicResponseDTO;
import com.example.tunehub.dto.SheetMusicUploadDTO;
import com.example.tunehub.model.SheetMusic;
import com.example.tunehub.service.InstrumentMapper;
import com.example.tunehub.service.SheetMusicCategoryMapper;
import com.example.tunehub.service.UsersMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {InstrumentMapper.class, SheetMusicCategoryMapper.class, UsersMapper.class})
public interface SheetMusicMapper {

    // *************************************************************
    // המרה מ-JPA ל-Response DTO (שליחה ללקוח)
    // *************************************************************

//    // אנו מניחים שיש שדה 'filePath' במודל ה-JPA המקורי שלך
//    // נשתמש בו כדי לבנות את ה-fileUrl שצריך להיות ב-Response DTO
//    @Mapping(target = "fileUrl", expression = "java(\"/api/sheet-music/download/\" + entity.getId())")
//    @Mapping(target = "user", source = "entity.user") // המרה באמצעות UserProfileMapper
//    SheetMusicResponseDTO toResponseDto(SheetMusic entity);

  //  List<SheetMusicResponseDTO> toResponseDtoList(List<SheetMusic> entities);

    @Mapping(target = "dateUploaded", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "hearts", constant = "0")
    @Mapping(target = "likes", constant = "0")
    @Mapping(target = "category", qualifiedByName = "toSheetMusicCategory")
    SheetMusic SheetMusicUploadDTOtoSheetMusic(SheetMusicUploadDTO s);


    @Mapping(
            target = "filePath",
            expression = "java(com.example.tunehub.service.FileUtils.docsToBase64(s.getFileName()))")
    SheetMusicResponseDTO sheetMusicToSheetMusicResponseDTO(SheetMusic s);

    List<SheetMusicResponseDTO> sheetMusicListToSheetMusicResponseDTOlist(List<SheetMusic> s);
}