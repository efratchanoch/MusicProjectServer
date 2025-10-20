package com.example.tunehub.service;

import com.example.tunehub.model.Comment;

import java.util.List;

public interface CommentRepository {
    List<Comment> findCommentByPostId(Long post_id);

    Comment findCommentById(Long id);

//    Void save(Comment comment);

    Comment save(Comment comment);
}
