package com.example.tunehub.service;

import com.example.tunehub.dto.CommentDTO;
import com.example.tunehub.dto.CommentUploadDTO;
import com.example.tunehub.model.Comment;
import com.example.tunehub.model.Post;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import com.example.tunehub.service.UsersMapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UsersMapper.class)
public interface CommentMapper {
    // CommentDTO CommentDTOtoComment(Comment c);

    //@Mapping(target = "profile.name", source = "user.name")

    @Mapping(
            target = "isLiked",
            expression = "java(likeRepo.existsByUserIdAndTargetTypeAndTargetId(currentUserId,  com.example.tunehub.model.ETargetType.COMMENT, c.getId()))")
    @Mapping(
            target = "likes",
            expression = "java(likeRepo.countByTargetTypeAndTargetId( com.example.tunehub.model.ETargetType.COMMENT, c.getId()))")
    //@Mapping(target = "profile.imageProfilePath", expression = "java(com.example.tunehub.service.FileUtils.imagesToBase64(users.getImagesPath()))")
    //@Mapping(target = "profile.id", source = "user.id")
    @Mapping(target = "profile", source = "user")
    CommentDTO commentToCommentDTO(
            Comment c,
            @Context Long currentUserId,
            @Context LikeRepository likeRepo);

    // מיפוי מה-UploadDTO ל-Entity
    // הערה: User ו-Post יוגדרו ידנית בסרביס לאחר שליפתם מה-DB
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "dateUploaded", ignore = true)
    @Mapping(target = "likes", constant = "0")
    Comment commentUploadDTOToComment(CommentUploadDTO dto);
}

