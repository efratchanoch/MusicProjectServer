package com.example.tunehub.controller;

import com.example.tunehub.model.Post;
import com.example.tunehub.service.FileUtils;
import com.example.tunehub.service.PostMapper;
import com.example.tunehub.service.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

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

    //Get
    @GetMapping("/postById/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        try {
            Post p = postRepository.findPostById(id);
            if (p == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(p, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getPosts() {
        try {
            List<Post> p = postRepository.findAll();
            if (p == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(p, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/postsByUserId/{id}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable Long id) {
        try {
            List<Post> p = postRepository.findAllByUserId(id);
            if (p == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(p, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/favoritePostsByUserId/{id}")
    public ResponseEntity<List<Post>> getFavoritePostsByUserId(@PathVariable Long id) {
        try {
            List<Post> p = postRepository.findAllByUsersFavorite_Id(id);
            if (p == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(p, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/newPosts")
    public ResponseEntity<List<Post>> getNewPosts() {
        try {
            List<Post> p = postRepository.findByDateUploaded(LocalDate.now());
            if (p == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(p, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/audio")
    public ResponseEntity<String> uploadAudio(@RequestParam("file") MultipartFile file) {
        try {
            FileUtils.uploadAudio(file);
            return ResponseEntity.ok("Audio uploaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading audio");
        }
    }

    @PostMapping("/video")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            FileUtils.uploadVideo(file);
            return ResponseEntity.ok("Video uploaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading video");
        }
    }
}
