package com.example.tunehub.controller;

import com.example.tunehub.service.CommentMapper;
import com.example.tunehub.service.CommentRepository;
import com.example.tunehub.service.PostMapper;
import com.example.tunehub.service.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
@CrossOrigin
public class PostController {
    private PostRepository postRepository;
    private PostMapper postMapper;

    @Autowired
    public PostController(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }
}
