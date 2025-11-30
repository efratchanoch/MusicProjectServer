package com.example.tunehub.service;

import com.example.tunehub.dto.SheetMusicResponseDTO;
import com.example.tunehub.dto.SheetMusicSearchDTO;
import com.example.tunehub.dto.SheetMusicUploadDTO;
import com.example.tunehub.model.SheetMusic;
import com.example.tunehub.service.InstrumentMapper;
import com.example.tunehub.service.SheetMusicCategoryMapper;
import com.example.tunehub.service.UsersMapper;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {InstrumentMapper.class, SheetMusicCategoryMapper.class, UsersMapper.class})
public interface SheetMusicMapper {
    @Mapping(target = "title", source = "title")
    @Mapping(target = "filePath", source = "fileName")
    @Mapping(
            target = "imageCoverName",
            expression = "java(com.example.tunehub.service.FileUtils.imageToBase64(s.getImageCoverName()))")
    @Mapping(
            target = "isLiked",
            expression = "java(likeRepo.existsByUserIdAndTargetTypeAndTargetId(currentUserId,  com.example.tunehub.model.ETargetType.SHEET_MUSIC, s.getId()))")
    @Mapping(
            target = "isFavorite",
            expression = "java(favRepo.existsByUserIdAndTargetTypeAndTargetId(currentUserId, com.example.tunehub.model.ETargetType.SHEET_MUSIC, s.getId()))")
    @Mapping(
            target = "likes",
            expression = "java(likeRepo.countByTargetTypeAndTargetId( com.example.tunehub.model.ETargetType.SHEET_MUSIC, s.getId()))")
    @Mapping(
            target = "hearts",
            expression = "java(favRepo.countByTargetTypeAndTargetId( com.example.tunehub.model.ETargetType.SHEET_MUSIC, s.getId()))")
    SheetMusicResponseDTO sheetMusicToSheetMusicResponseDTO(
            SheetMusic s,
            @Context Long currentUserId,
            @Context LikeRepository likeRepo,
            @Context FavoriteRepository favRepo);

    List<SheetMusicResponseDTO> sheetMusicListToSheetMusicResponseDTOlist(
            List<SheetMusic> s,
            @Context Long currentUserId,
            @Context LikeRepository likeRepo,
            @Context FavoriteRepository favRepo);

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
    @Mapping(target = "instruments", ignore = true)
    @Mapping(target = "categories", ignore = true)
    SheetMusic SheetMusicUploadDTOtoSheetMusic(SheetMusicUploadDTO s);

    @Mapping(
            target = "imageCoverName",
            expression = "java(com.example.tunehub.service.FileUtils.imageToBase64(s.getImageCoverName()))")
    @Mapping(target = "userName", source = "user.name")
    SheetMusicSearchDTO sheetMusicToSheetMusicSearchDTO(SheetMusic s);

    List<SheetMusicSearchDTO> sheetMusicListToSheetMusicSearchDTOList(List<SheetMusic> s);
}
