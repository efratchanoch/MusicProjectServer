package com.example.tunehub.service;

import com.example.tunehub.model.Comment;
import com.example.tunehub.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long post_id);

    Comment findCommentById(Long id);

    Comment save(Comment comment);
}
