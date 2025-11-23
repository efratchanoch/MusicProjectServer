package com.example.tunehub.service;

import com.example.tunehub.model.Post;
import com.example.tunehub.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface PostRepository  extends JpaRepository<Post, Long> {
    Post findUsersById(Long id);

    Post findPostById(Long id);

    List<Post> findAllByUserId(Long id);

    List<Post> findByDateUploaded(LocalDate dateUploaded);

    List<Post> findAllByUsersFavorite_Id(Long id);

    List<Post> findAllPostByTitle(String title);

    @Modifying
    @Query("UPDATE Post p SET p.likes = :count WHERE p.id = :id")
    @Transactional
    void updateLikeCount(@Param("id") Long postId, @Param("count") int count);
}
