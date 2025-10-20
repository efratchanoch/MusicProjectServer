package com.example.tunehub.service;

import com.example.tunehub.model.Post;

public interface PostRepository {
    Post findPostId(Long post_id);
}
