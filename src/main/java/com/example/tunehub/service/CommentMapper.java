package com.example.tunehub.service;

import com.example.tunehub.dto.CommentDTO;
import com.example.tunehub.dto.CommentUploadDTO;
import com.example.tunehub.model.Comment;
import org.mapstruct.Mapper;
import com.example.tunehub.service.UsersMapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
   // CommentDTO CommentDTOtoComment(Comment c);

    @Mapping(target = "profile.name", source = "user.name")
    @Mapping(target = "profile.imageProfilePath", source = "user.imageProfilePath")
    @Mapping(target = "profile.id", source = "user.id")
    CommentDTO commentToCommentDTO(Comment comment);

    // מיפוי מה-UploadDTO ל-Entity
    // הערה: User ו-Post יוגדרו ידנית בסרביס לאחר שליפתם מה-DB
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "dateUploaded", ignore = true)
    @Mapping(target = "likes", ignore = true)
    Comment commentUploadDTOToComment(CommentUploadDTO dto);
}

