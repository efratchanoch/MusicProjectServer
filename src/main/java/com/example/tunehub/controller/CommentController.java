package com.example.tunehub.controller;

import com.example.tunehub.dto.CommentDTO;
import com.example.tunehub.dto.CommentPageDTO;
import com.example.tunehub.dto.CommentUploadDTO;
import com.example.tunehub.model.Comment;
import com.example.tunehub.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    // Get
    @GetMapping("/commentById/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        try {
            Comment comment = commentService.getCommentById(id);
            return (comment != null)
                    ? new ResponseEntity<>(comment, HttpStatus.OK)
                    : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/byPost/{postId}/paged")
    public ResponseEntity<CommentPageDTO> getCommentsByPostPaged(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            CommentPageDTO result = commentService.getCommentsByPostPaged(postId, page, size);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Post
    @PostMapping("/upload")
    public ResponseEntity<CommentDTO> uploadComment(@RequestBody CommentUploadDTO dto) {
        try {
            CommentDTO created = commentService.uploadComment(dto);
            return new ResponseEntity<>(created, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

