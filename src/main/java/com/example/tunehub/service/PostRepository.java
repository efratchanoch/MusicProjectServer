package com.example.tunehub.service;

import com.example.tunehub.model.Post;
import com.example.tunehub.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PostRepository  extends JpaRepository<Post, Long> {
    Post findUsersById(Long id);

    Post findPostById(Long id);

    List<Post> findAllByUserId(Long id);

    List<Post> findByDateUploaded(LocalDate dateUploaded);

    List<Post> findAllByUsersFavorite(Long id);
}
