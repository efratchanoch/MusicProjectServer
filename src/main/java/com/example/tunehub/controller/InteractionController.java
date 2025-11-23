package com.example.tunehub.controller;

import com.example.tunehub.dto.NotificationSimpleDTO;
import com.example.tunehub.model.*; // ייבוא ה-Entities
import com.example.tunehub.security.CustomUserDetails; // ייבוא CustomUserDetails
import com.example.tunehub.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/interaction")
public class InteractionController {

    private final PostRepository postRepository;
    private final SheetMusicRepository sheetMusicRepository;
    private final UsersRepository usersRepository;
    private final LikeRepository likeRepository;
    private final NotificationRepository notificationRepository;
    private final FavoriteRepository favoriteRepository;
    private final FollowRepository followRepository;
    private final AuthService authService;

    @Autowired
    public InteractionController(PostRepository postRepository, SheetMusicRepository sheetMusicRepository, UsersRepository usersRepository, LikeRepository likeRepository, NotificationRepository notificationRepository, FavoriteRepository favoriteRepository, FollowRepository followRepository, AuthService authService) {
        this.postRepository = postRepository;
        this.sheetMusicRepository = sheetMusicRepository;
        this.usersRepository = usersRepository;
        this.likeRepository = likeRepository;
        this.notificationRepository = notificationRepository;
        this.favoriteRepository = favoriteRepository;
        this.followRepository = followRepository;
        this.authService = authService;
    }










    private Users getContentOwner(ETargetType targetType, Long targetId) {
        switch (targetType) {
            case POST:
                return postRepository.findById(targetId)
                        .map(Post::getUser) // הנחה: getAuthor() מחזיר Users
                        .orElse(null);

            case SHEET_MUSIC:
                return sheetMusicRepository.findById(targetId)
                        .map(SheetMusic::getUser) // הנחה: getAuthor() מחזיר Users
                        .orElse(null);

            case USER:
                // במקרה של Follow, הבעלים הוא המשתמש הנעקב.
                return usersRepository.findById(targetId).orElse(null);

            default:
                return null;
        }
    }


    private void updateContentCount(ETargetType targetType, Long targetId, int newCount) {
        switch (targetType) {
            case POST:
                postRepository.updateLikeCount(targetId, newCount);
                break;

            case SHEET_MUSIC:
                sheetMusicRepository.updateLikeCount(targetId, newCount);
                break;

            case USER:
                usersRepository.updateFollowerCount(targetId, newCount);
                break;

            default:
                throw new IllegalArgumentException("Unsupported ETargetType for count update: " + targetType);
        }
    }

    // LIKE OPERATIONS
    @PostMapping("/like/add/{targetType}/{targetId}")
    public ResponseEntity<NotificationSimpleDTO> addLike(
            @PathVariable ETargetType targetType,
            @PathVariable Long targetId) {

        Long currentUserId = authService.getCurrentUserId();

        try {
            if (!likeRepository.existsByUserIdAndTargetTypeAndTargetId(currentUserId, targetType, targetId)) {

                Like newLike = new Like(currentUserId, targetType, targetId);
                likeRepository.save(newLike);

                Users contentOwner = getContentOwner(targetType, targetId);
                if (contentOwner != null) {
                    Users currentUserEntity = authService.getCurrentUser();

                    notificationRepository.save(new Notification(
                            ENotificationType.NEW_LIKE,
                            contentOwner,
                            currentUserEntity,
                            targetType,
                            targetId
                    ));
                }
            }

            int newCount = likeRepository.countByTargetTypeAndTargetId(targetType, targetId);
            updateContentCount(targetType, targetId, newCount);

            return new ResponseEntity<>(new NotificationSimpleDTO(targetId, newCount, false), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/like/remove/{targetType}/{targetId}")
    public ResponseEntity<NotificationSimpleDTO> removeLike(
            @PathVariable ETargetType targetType,
            @PathVariable Long targetId) {

        Long currentUserId = authService.getCurrentUserId();

        try {
            Optional<Like> existingLike = likeRepository.findByUserIdAndTargetTypeAndTargetId(
                    currentUserId, targetType, targetId);

            if (existingLike.isPresent()) {
                likeRepository.delete(existingLike.get());
            }


            int newCount = likeRepository.countByTargetTypeAndTargetId(targetType, targetId);
            updateContentCount(targetType, targetId, newCount);

            return new ResponseEntity<>(new NotificationSimpleDTO(targetId, newCount, false), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // FOLLOW OPERATIONS
    @PostMapping("/follow/toggle/{targetUserId}")
    public ResponseEntity<String> toggleFollowRequest(@PathVariable Long targetUserId) {
        Users follower = authService.getCurrentUser();

        try {
            Optional<Follow> existingFollow = followRepository.findByFollowerIdAndFollowingId(
                    follower.getId(), targetUserId);

            if (existingFollow.isPresent()) {
                followRepository.delete(existingFollow.get());

                Users followingUser = usersRepository.findById(targetUserId).orElse(null);
                if (followingUser != null) {
                    notificationRepository.save(new Notification(
                            ENotificationType.FOLLOW_REQUEST,//*הורידה עוקב
                            followingUser,
                            follower,
                            ETargetType.USER,
                            follower.getId()
                    ));
                }
                return new ResponseEntity<>("Unfollowed successfully.", HttpStatus.OK);

            } else {
                Follow newFollow = new Follow(follower.getId(), targetUserId, EFollowStatus.PENDING);
                followRepository.save(newFollow);

                Users followingUser = usersRepository.findById(targetUserId).orElseThrow(() -> new NoSuchElementException("Target user not found"));

                notificationRepository.save(new Notification(
                        ENotificationType.FOLLOW_REQUEST,
                        followingUser,
                        follower,
                        ETargetType.USER,
                        follower.getId()
                ));

                return new ResponseEntity<>("Follow Request Sent (Pending Approval).", HttpStatus.ACCEPTED);
            }
        } catch (NoSuchElementException e) {
            System.err.println("Resource not found: " + e.getMessage());
            return new ResponseEntity<>("Target user not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.err.println("Error toggling follow: " + e.getMessage());
            return new ResponseEntity<>("Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Tracking confirmation
    @PostMapping("/follow/approve/{followerId}")
    public ResponseEntity<String> approveFollow(@PathVariable Long followerId) {
        Users followingUser = authService.getCurrentUser();

        try {
            Optional<Follow> existingFollow = followRepository.findByFollowerIdAndFollowingIdAndStatus(
                    followerId, followingUser.getId(), EFollowStatus.PENDING);

            Follow follow = existingFollow.orElseThrow(() -> new NoSuchElementException("לא נמצאה בקשת מעקב ממתינה."));


            follow.setStatus(EFollowStatus.APPROVED);
            followRepository.save(follow);

            // 3. התראה למשתמש X שהוא אושר לעקוב
            Users follower = usersRepository.findById(followerId).orElse(null);
            if (follower != null) {
                notificationRepository.save(new Notification(
                        ENotificationType.FOLLOW_APPROVED,
                        follower, // מי מקבל: המשתמש שביקש
                        followingUser, // מי יזם: המשתמש שאישר
                        ETargetType.USER,
                        followingUser.getId()
                ));
            }

            return new ResponseEntity<>("בקשת מעקב אושרה.", HttpStatus.OK);

        } catch (NoSuchElementException e) {
            System.err.println("Resource not found: " + e.getMessage());
            return new ResponseEntity<>("לא נמצאה בקשת מעקב ממתינה.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.err.println("Error approving follow: " + e.getMessage());
            return new ResponseEntity<>("שגיאת שרת פנימית.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ====================================================================
    //  FAVORITE OPERATIONS (הוספה/הסרה)
    // ====================================================================

    @PostMapping("/favorite/add/{targetType}/{targetId}")
    public ResponseEntity<String> addFavorite(
            @PathVariable ETargetType targetType,
            @PathVariable Long targetId) {

        Long currentUserId = authService.getCurrentUserId();

        try {
            if (!favoriteRepository.existsByUserIdAndTargetTypeAndTargetId(
                    currentUserId, targetType, targetId)) {

                Favorite newFavorite = new Favorite(currentUserId, targetType, targetId);
                favoriteRepository.save(newFavorite);

                // יצירת התראה לבעל התוכן
                Users contentOwner = getContentOwner(targetType, targetId);
                if (contentOwner != null) {
                    Users currentUserEntity = authService.getCurrentUser(); // נדרש Entity מלא

                    notificationRepository.save(new Notification(
                            ENotificationType.FAVORITE_ADDED,
                            contentOwner,
                            currentUserEntity,
                            targetType,
                            targetId
                    ));
                }
            }

            return new ResponseEntity<>("הוסף למועדפים בהצלחה.", HttpStatus.CREATED);

        } catch (Exception e) {
            System.err.println("Error adding favorite: " + e.getMessage());
            return new ResponseEntity<>("שגיאת שרת פנימית.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/favorite/remove/{targetType}/{targetId}")
    public ResponseEntity<String> removeFavorite(
            @PathVariable ETargetType targetType,
            @PathVariable Long targetId) {

        Long currentUserId = authService.getCurrentUserId();

        try {
            Optional<Favorite> existingFavorite = favoriteRepository.findByUserIdAndTargetTypeAndTargetId(
                    currentUserId, targetType, targetId);

            if (existingFavorite.isPresent()) {
                favoriteRepository.delete(existingFavorite.get());

                // אין צורך בהתראת "הסרת מועדף" בדרך כלל, אך אפשר להוסיף אם נדרש.
            }

            // מחזירים OK גם אם לא נמצא, לשמירה על אידמפוטנטיות (Idempotence)
            return new ResponseEntity<>("הוסר מהמועדפים.", HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("Error removing favorite: " + e.getMessage());
            return new ResponseEntity<>("שגיאת שרת פנימית.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}