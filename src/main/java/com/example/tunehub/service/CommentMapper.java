package com.example.tunehub.service;

import com.example.tunehub.dto.CommentDTO;
import com.example.tunehub.model.Comment;
import org.mapstruct.Mapper;
import com.example.tunehub.service.UsersMapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDTO CommentDTOtoComment(Comment c);

}

