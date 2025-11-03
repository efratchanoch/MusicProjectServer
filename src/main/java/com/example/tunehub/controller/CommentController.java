package com.example.tunehub.controller;

import com.example.tunehub.model.Comment;
import com.example.tunehub.model.Post;
import com.example.tunehub.model.Users;
import com.example.tunehub.service.CommentMapper;
import com.example.tunehub.service.CommentRepository;
import com.example.tunehub.service.PostRepository;
import com.example.tunehub.service.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@CrossOrigin
public class CommentController {
    private CommentRepository commentRepository;
    private CommentMapper commentMapper;
    private PostRepository postRepository;

    @Autowired
    public CommentController(CommentRepository commentRepository, CommentMapper commentMapper, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.postRepository = postRepository;
    }


    //Get
    @GetMapping("/commentById/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        try {
            Comment c = commentRepository.findCommentById(id);
            if (c == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(c, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/commentsByPostId/{post_id}")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long post_id) {
        try {
            List<Comment> c = commentRepository.findByPostId(post_id);
            if (c == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(c, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }








    //Post
    //Put
    //Delete


    //removeLike
    @DeleteMapping("/removeLike/{comment_id}")
    public ResponseEntity<Void> removeLike(@PathVariable Long comment_id) {
        try {
            Comment c = commentRepository.findCommentById(comment_id);
            if (c == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (c.getLikes() > 0) {
                c.setLikes(c.getLikes() - 1);
                commentRepository.save(c);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //addCommentToPost
    @PostMapping("/addCommentToPost/{post_id}")
    public ResponseEntity<Comment> addCommentToPost(
            @PathVariable Long post_id,
            @RequestBody Comment c) {
        try {
            Post p = postRepository.findUsersById(post_id);
            if (p == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            c.setPost(p);
            Comment saved = commentRepository.save(c);

            return new ResponseEntity<>(saved, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //מחיקה בכל פעם אחד
    @DeleteMapping("/comment")
    public ResponseEntity DeleteComment(@RequestBody Comment comment){
        try{
            if (commentRepository.existsById(comment.getId())) {
                commentRepository.delete(comment);
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
