package com.example.tunehub.controller;

import com.example.tunehub.dto.PostResponseDTO;
import com.example.tunehub.dto.PostUploadDTO;
import com.example.tunehub.model.*;
import com.example.tunehub.security.CustomUserDetails;
import com.example.tunehub.service.*;
import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final LikeRepository likeRepository;
    private final FavoriteRepository favoriteRepository;
    private PostRepository postRepository;
    private PostMapper postMapper;
    private UsersRepository usersRepository;
    private AuthService authService;
    private NotificationService notificationService;

    @Autowired
    public PostController(LikeRepository likeRepository, FavoriteRepository favoriteRepository, PostRepository postRepository, PostMapper postMapper, UsersRepository usersRepository, AuthService authService, NotificationService notificationService) {
        this.likeRepository = likeRepository;
        this.favoriteRepository = favoriteRepository;
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.usersRepository = usersRepository;
        this.authService = authService;
        this.notificationService = notificationService;
    }





    //Get
    @GetMapping("/postById/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            Post p = postRepository.findPostById(id);
            if (p == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(postMapper.postToPostResponseDTO(p, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDTO>> getPosts() {
        try {
            Long currentUserId = authService.getCurrentUserId();
            List<Post> p = postRepository.findAll();
            if (p.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(postMapper.postListToPostResponseDTOlist(p, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/postsByUserId/{id}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByUserId(@PathVariable Long id) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            List<Post> p = postRepository.findAllByUserId(id);
            if (p == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(postMapper.postListToPostResponseDTOlist(p, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//
//    @GetMapping("/favoritePostsByUserId/{id}")
//    public ResponseEntity<List<PostResponseDTO>> getFavoritePostsByUserId(@PathVariable Long id) {
//        try {
//            Long currentUserId = authService.getCurrentUserId();
//            List<Post> p = postRepository.findAllByUsersFavorite_Id(id);
//            if (p == null) {
//                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//            }
//            return new ResponseEntity<>(postMapper.postListToPostResponseDTOlist(p, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    @GetMapping("/newPosts")
    public ResponseEntity<List<PostResponseDTO>> getNewPosts() {
        try {
            Long currentUserId = authService.getCurrentUserId();
            List<Post> p = postRepository.findByDateUploaded(LocalDate.now());
            if (p == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(postMapper.postListToPostResponseDTOlist(p, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/postsByTitle/{title}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByName(@PathVariable String title) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            List<Post> p = postRepository.findAllByTitleContainingIgnoreCase(title);
            if (p.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(postMapper.postListToPostResponseDTOlist(p, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/postsByDate/{date}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByDate(@PathVariable LocalDate Date) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            List<Post> p = postRepository.findByDateUploaded(Date);
            if (p.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(postMapper.postListToPostResponseDTOlist(p, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
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
//        CustomUserDetails currentUserDetails = (CustomUserDetails) SecurityContextHolder
//                .getContext().getAuthentication().getPrincipal();
//        Users user = usersRepository.findUsersByName(currentUserDetails.getUsername());

            Users user = authService.getCurrentUser();
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
            PostResponseDTO responseDTO = postMapper.postToPostResponseDTO(post, user.getId(), likeRepository, favoriteRepository);

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


    //Delete
    @DeleteMapping("/deletePostByPostId/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN' ,'ROLE_SUPER_ADMIN')")
    @Transactional // מבטיח שכל הפעולות מתבצעות כיחידה אחת
    public ResponseEntity<?> deletePostByPostId(@PathVariable Long id) {
        try {
            Post p = postRepository.findPostById(id);
            if (p == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Users postOwner = p.getUser(); // מזהה את בעל הפוסט
            Users adminUser = authService.getCurrentUser(); // **צריך פונקציה שמביאה את האדמין המחובר**

            // 1. מחיקת כל האינטראקציות (לייקים/מועדפים) הקשורות לפוסט
            // יש להשתמש ב-InteractionRepository (או InteractionService)
            likeRepository.deleteAllByTargetTypeAndTargetId(ETargetType.POST, id);
            favoriteRepository.deleteAllByTargetTypeAndTargetId(ETargetType.POST, id);

            // 2. יצירת ושליחת התראה לבעל הפוסט

            notificationService.createNotification(
                    ENotificationType.ADMIN_WARNING_POST,
                    postOwner,
                    adminUser,
                    ETargetType.POST,
                    id
            );

            // 3. מחיקת הפוסט
            postRepository.deleteById(id);

            // **הוספנו הודעה להחזרה - אין צורך ב-NO_CONTENT אלא ב-OK עם הודעה**
            return new ResponseEntity<>("Post deleted and user notified.", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // ... בתוך AdminController או PostController ...

    @PostMapping("/admin/sendPostOwnerWarningNotification/{postId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN' ,'ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> sendPostOwnerWarningNotification(@PathVariable Long postId, @RequestParam(required = false) String customMessage) {
        try {
            Post p = postRepository.findPostById(postId);
            if (p == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Users postOwner = p.getUser();
            Users adminUser = authService.getCurrentUser(); // **האדמין ששולח**

            Notification notification = new Notification(
                    ENotificationType.ADMIN_WARNING_POST,
                    postOwner,
                    adminUser,
                    ETargetType.POST,
                    postId
            );

            // הוספת הודעה מותאמת אישית אם נשלחה כפרמטר
            if (customMessage != null && !customMessage.trim().isEmpty()) {
                notification.setMessage(customMessage);
            } else {
                // יקבע את הודעת ברירת המחדל (כפי שהוגדר ב-Notification.java)
                notification.setTitleAndMessageBasedOnType(ENotificationType.ADMIN_WARNING_POST, adminUser);
            }

            notificationService.saveNotification(notification); // נניח שזו הפונקציה לשמירה ושליחה

            return new ResponseEntity<>("Warning notification sent to post owner.", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
