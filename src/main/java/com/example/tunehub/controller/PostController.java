package com.example.tunehub.controller;

import com.example.tunehub.dto.PostResponseDTO;
import com.example.tunehub.dto.PostUploadDTO;
import com.example.tunehub.model.Post;
import com.example.tunehub.model.Users;
import com.example.tunehub.security.CustomUserDetails;
import com.example.tunehub.service.FileUtils;
import com.example.tunehub.service.PostMapper;
import com.example.tunehub.service.PostRepository;
import com.example.tunehub.service.UsersRepository;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    private PostRepository postRepository;
    private PostMapper postMapper;
    private UsersRepository usersRepository;

    @Autowired
    public PostController(PostRepository postRepository, PostMapper postMapper, UsersRepository usersRepository) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.usersRepository = usersRepository;
    }


    //Get
    @GetMapping("/postById/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        try {
            Post p = postRepository.findPostById(id);
            if (p == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(postMapper.postToPostResponseDTO(p), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDTO>> getPosts() {
        try {
            List<Post> p = postRepository.findAll();
            if (p.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(postMapper.postListToPostResponseDTOlist(p), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/postsByUserId/{id}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByUserId(@PathVariable Long id) {
        try {
            List<Post> p = postRepository.findAllByUserId(id);
            if (p == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(postMapper.postListToPostResponseDTOlist(p), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/favoritePostsByUserId/{id}")
    public ResponseEntity<List<PostResponseDTO>> getFavoritePostsByUserId(@PathVariable Long id) {
        try {
            List<Post> p = postRepository.findAllByUsersFavorite_Id(id);
            if (p == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(postMapper.postListToPostResponseDTOlist(p), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/newPosts")
    public ResponseEntity<List<PostResponseDTO>> getNewPosts() {
        try {
            List<Post> p = postRepository.findByDateUploaded(LocalDate.now());
            if (p == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(postMapper.postListToPostResponseDTOlist(p), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/postsByTitle/{title}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByName(@PathVariable String title) {
        try {
            List<Post> p = postRepository.findAllPostByTitle(title);
            if (p.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(postMapper.postListToPostResponseDTOlist(p), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/postsByDate/{date}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByDate(@PathVariable LocalDate Date) {
        try {
            List<Post> p = postRepository.findByDateUploaded(Date);
            if (p.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(postMapper.postListToPostResponseDTOlist(p), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Post


//    @PostMapping("/audio")
//    public ResponseEntity<String> uploadAudio(@RequestParam("file") MultipartFile file) {
//        try {
//            FileUtils.uploadAudio(file);
//            return ResponseEntity.ok("Audio uploaded successfully!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error uploading audio");
//        }
//    }

//    @GetMapping("/audio/{audioPath}")
//    public ResponseEntity<Resource> getReport(@PathVariable String audioPath) throws IOException {
//        InputStreamResource resource=FileUtils.getAudio(audioPath);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + audioPath + "\"")
//                .contentType(MediaType.parseMediaType("audio/mpeg"))
//                .body((Resource) resource);
//    }
//
//    @PostMapping("/video")
//    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
//        try {
//            FileUtils.uploadVideo(file);
//            return ResponseEntity.ok("Video uploaded successfully!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error uploading video");
//        }
//    }
@PostMapping("/uploadPost")
public ResponseEntity<PostResponseDTO> createPost(
        @RequestPart("data") PostUploadDTO dto,
        @RequestPart(value = "images", required = false) List<MultipartFile> images,
        @RequestPart(value = "audio", required = false) MultipartFile audio,
        @RequestPart(value = "video", required = false) MultipartFile video) {

    try {
        // 🔑 קבלת המשתמש מה־JWT דרך SecurityContext
        CustomUserDetails currentUserDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Users user = usersRepository.findUsersByName(currentUserDetails.getUsername());

        // Convert DTO to Entity
        Post post = postMapper.postUploadDTOtoPost(dto);
        post.setUser(user); // משתמש קיים מה־JWT

        // ==================== Images ====================
        if (images != null && !images.isEmpty()) {
            List<String> imageNames = new ArrayList<>();
            for (MultipartFile img : images) {
                String uniqueName = FileUtils.generateUniqueFileName(img);
                FileUtils.uploadImage(img, uniqueName);
                imageNames.add(uniqueName);
            }
            post.setImagesPath(imageNames);
        }

        // ==================== Audio ====================
        if (audio != null) {
            String uniqueAudioName = FileUtils.generateUniqueFileName(audio);
            FileUtils.uploadAudio(audio, uniqueAudioName);
            post.setAudioPath(uniqueAudioName);
        }

        // ==================== Video ====================
        if (video != null) {
            String uniqueVideoName = FileUtils.generateUniqueFileName(video);
            FileUtils.uploadVideo(video, uniqueVideoName);
            post.setVideoPath(uniqueVideoName);
        }

        // Save to database
        postRepository.save(post);

        // Build DTO
        PostResponseDTO responseDTO = postMapper.postToPostResponseDTO(post);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


    // ==================== הזרמת אודיו ====================
    @GetMapping("/audio/{audioPath}")
    public ResponseEntity<Resource> getAudio(@PathVariable String audioPath) throws IOException {
        InputStreamResource resource = FileUtils.getAudio(audioPath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + audioPath + "\"")
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(resource);
    }

    // ==================== הזרמת וידאו ====================
    @GetMapping("/video/{videoPath}")
    public ResponseEntity<Resource> getVideo(@PathVariable String videoPath) throws IOException {
        InputStreamResource resource = FileUtils.getVideo(videoPath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + videoPath + "\"")
                .contentType(MediaType.parseMediaType("video/mp4"))
                .body(resource);
    }

    // ==================== הזרמת מסמכים / PDF ====================
    @GetMapping("/documents/{docPath}")
    public ResponseEntity<Resource> getDocument(@PathVariable String docPath) throws IOException {
        InputStreamResource resource = FileUtils.getDocument(docPath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + docPath + "\"")
                .contentType(MediaType.APPLICATION_PDF) // או MediaType.APPLICATION_OCTET_STREAM אם לא PDF
                .body(resource);
    }

    // ==================== הזרמת תמונה ====================
    @GetMapping("/image/{imagePath}")
    public ResponseEntity<Resource> getImage(@PathVariable String imagePath) throws IOException {
        InputStreamResource resource = new InputStreamResource(
                new ByteArrayInputStream(FileUtils.imageToBase64(imagePath).getBytes())
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imagePath + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }


}
