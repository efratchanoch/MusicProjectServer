package com.example.tunehub.controller;

import com.example.tunehub.service.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;


public class PostController {
    private final PostController postController;

//    @Autowired
    public PostController(PostController postController) {
        this.postController = postController;
    }
}
