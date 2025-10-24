package com.example.tunehub.service;

import com.example.tunehub.model.Post;
import com.example.tunehub.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository  extends JpaRepository<Post, Long> {
    Post findUsersById(Long post_id);
}
