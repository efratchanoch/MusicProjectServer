package com.example.tunehub.service;

import com.example.tunehub.model.Comment;
import com.example.tunehub.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long post_id);

    Comment findCommentById(Long id);

    Comment save(Comment comment);

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId ORDER BY c.dateUploaded DESC")
    Page<Comment> findByPostIdOrderByDateUploadedDesc(@Param("postId") Long postId, Pageable pageable);

    List<Comment> findByPostIdOrderByDateUploadedAsc(Long postId);



    @Modifying
    @Query("UPDATE Comment c SET c.likes = :count WHERE c.id = :id")
    @Transactional
    void updateLikeCount(@Param("id") Long  commentId, @Param("count") int count);
}
