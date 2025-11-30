package com.example.tunehub.controller;

import com.example.tunehub.dto.CommentDTO;
import com.example.tunehub.dto.CommentPageDTO;
import com.example.tunehub.dto.CommentUploadDTO;
import com.example.tunehub.model.Comment;
import com.example.tunehub.model.Post;
import com.example.tunehub.model.Users;
import com.example.tunehub.security.CustomUserDetails;
import com.example.tunehub.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private UsersRepository  usersRepository;
    private CommentRepository commentRepository;
    private CommentMapper commentMapper;
    private PostRepository postRepository;
    private LikeRepository likeRepository;
    private AuthService authService;

    @Autowired
    public CommentController(UsersRepository usersRepository, CommentRepository commentRepository, CommentMapper commentMapper, PostRepository postRepository, LikeRepository likeRepository, AuthService authService) {
        this.usersRepository = usersRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.authService = authService;
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

//    @GetMapping("/commentsByPostId/{post_id}")
//    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long post_id) {
//        try {
//            List<Comment> c = commentRepository.findByPostId(post_id);
//            if (c == null) {
//                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//            }
//            return new ResponseEntity<>(c, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }










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
//    @PostMapping("/addCommentToPost/{post_id}")
//    public ResponseEntity<Comment> addCommentToPost(
//            @PathVariable Long post_id,
//            @RequestBody Comment c) {
//        try {
//            Post p = postRepository.findUsersById(post_id);
//            if (p == null) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//
//            c.setPost(p);
//            Comment saved = commentRepository.save(c);
//
//            return new ResponseEntity<>(saved, HttpStatus.CREATED);
//
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    // CommentController.java

//    @PostMapping("/add")
//    public ResponseEntity<CommentDTO> addComment(
//            @RequestBody CommentUploadDTO dto,
//            @AuthenticationPrincipal CustomUserDetails currentUser) {
//
//        if (currentUser == null) {
//            // 401 Unauthorized: המשתמש לא מאומת
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//
//        try {
//            // --- לוגיקה עסקית ---
//            Long userId = currentUser.getId();
//            Long postId = dto.getPostId();
//
//            // שליפת המשתמש
//            Optional<Users> userOpt = usersRepository.findById(userId);
//            if (userOpt.isEmpty()) {
//                // 404 Not Found: המשתמש לא נמצא (או נמחק)
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//
//            // שליפת הפוסט
//            Optional<Post> postOpt = postRepository.findById(postId);
//            if (postOpt.isEmpty()) {
//                // 404 Not Found: הפוסט לא נמצא
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//
//            // 2. יצירת Entity ומיפוי נתונים מה-DTO
//            Comment comment = commentMapper.commentUploadDTOToComment(dto);
//
//            // 3. הגדרת שדות ברירת מחדל ואסוציאציות
//            comment.setUser(userOpt.get());
//            comment.setPost(postOpt.get());
//            comment.setDateUploaded(LocalDate.now());
//            comment.setLikes(0);
//
//            // 4. שמירה בבסיס הנתונים
//            Comment savedComment = commentRepository.save(comment);
//
//            // 5. המרה ל-DTO והחזרת תגובה (201 Created)
//            CommentDTO responseDTO = commentMapper.commentToCommentDTO(savedComment);
//            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
//
//        } catch (Exception e) {
//            // 500 Internal Server Error: לכל שגיאה בלתי צפויה
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    // 2. 📖 פונקציה לשליפת תגובות (Get)
    // אם אין לך Service, תצטרך להטמיע את הלוגיקה הזו כאן
//    @GetMapping("/{postId}")
//    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long postId) {
//        // ... לוגיקת שליפה והמרה ל-DTO באמצעות commentRepository.findByPostId...
//        // ... והשימוש ב-commentMapper...
//
//        List<Comment> comments = commentRepository.findByPostIdOrderByDateUploadedAsc(postId);
//        List<CommentDTO> dtos = comments.stream()
//                .map(commentMapper::commentToCommentDTO)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(dtos);
//    }
//}
    //מחיקה בכל פעם אחד
//    @DeleteMapping("/comment")
//    public ResponseEntity DeleteComment(@RequestBody Comment comment){
//        try{
//            if (commentRepository.existsById(comment.getId())) {
//                commentRepository.delete(comment);
//                return new ResponseEntity(HttpStatus.NO_CONTENT);
//            }
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        catch(Exception e){
//            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PostMapping("/upload")
    public CommentDTO uploadComment(@RequestBody CommentUploadDTO dto) {
        // מוצאים את הפוסט והמשתמש
        Post post = postRepository.findById(dto.postId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Users user = authService.getCurrentUser();

        // ממפים את ה-UploadDTO ל-Entity בעזרת Mapper
        Comment comment = commentMapper.commentUploadDTOToComment(dto);
        comment.setPost(post);
        comment.setUser(user);
        comment.setDateUploaded(LocalDate.now());


        // שומרים את התגובה במסד
        Comment savedComment = commentRepository.save(comment);

        // ממפים את ה-Entity ל-DTO בעזרת Mapper ומחזירים
        return commentMapper.commentToCommentDTO(savedComment,user.getId(),likeRepository);
    }

    @GetMapping("/byPost/{postId}/paged")
    public ResponseEntity<CommentPageDTO> getCommentsByPostPaged(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            Pageable pageable = PageRequest.of(page, size);
            Page<Comment> commentsPage = commentRepository.findByPostIdOrderByDateUploadedDesc(post.getId(), pageable);

            // המרה ל-DTO
            List<CommentDTO> dtos = commentsPage.getContent()
                    .stream()
                    .map(comment -> commentMapper.commentToCommentDTO(comment, authService.getCurrentUserId(), likeRepository))
                    .collect(Collectors.toList());

            // הכנת DTO עם מידע על מספר הדפים והתגובות
            CommentPageDTO response = new CommentPageDTO();
            response.setComments(dtos);
            response.setTotalElements(commentsPage.getTotalElements());
            response.setTotalPages(commentsPage.getTotalPages());
            response.setCurrentPage(page);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
