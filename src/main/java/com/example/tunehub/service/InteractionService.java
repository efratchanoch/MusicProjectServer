package com.example.tunehub.service;

import com.example.tunehub.dto.NotificationSimpleDTO;
import com.example.tunehub.model.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class InteractionService {

    private final PostRepository postRepository;
    private final SheetMusicRepository sheetMusicRepository;
    private final UsersRepository usersRepository;
    private final LikeRepository likeRepository;
    private final NotificationRepository notificationRepository;
    private final FavoriteRepository favoriteRepository;
    private final FollowRepository followRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;

    @Autowired
    public InteractionService(PostRepository postRepository, SheetMusicRepository sheetMusicRepository,
                              UsersRepository usersRepository, LikeRepository likeRepository,
                              NotificationRepository notificationRepository, FavoriteRepository favoriteRepository,
                              FollowRepository followRepository, AuthService authService,
                              CommentRepository commentRepository, NotificationService notificationService) {

        this.postRepository = postRepository;
        this.sheetMusicRepository = sheetMusicRepository;
        this.usersRepository = usersRepository;
        this.likeRepository = likeRepository;
        this.notificationRepository = notificationRepository;
        this.favoriteRepository = favoriteRepository;
        this.followRepository = followRepository;
        this.authService = authService;
        this.commentRepository = commentRepository;
        this.notificationService = notificationService;
    }


    private Users getContentOwner(ETargetType targetType, Long targetId) {
        switch (targetType) {
            case POST:
                return postRepository.findById(targetId)
                        .map(Post::getUser)
                        .orElse(null);

            case SHEET_MUSIC:
                return sheetMusicRepository.findById(targetId)
                        .map(SheetMusic::getUser)
                        .orElse(null);

            case USER:
                return usersRepository.findById(targetId).orElse(null);

            case COMMENT:
                return commentRepository.findById(targetId)
                        .map(Comment::getUser)
                        .orElse(null);
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

            case COMMENT:
                commentRepository.updateLikeCount(targetId, newCount);
                break;

            default:
                throw new IllegalArgumentException("Unsupported ETargetType: " + targetType);
        }
    }

    // LIKE *************************************************************
// אני כותבת פה הערות כדי שנבין מהזה עושה אחכ למחוק הכל כמובן
    public ResponseEntity<?> addLike(ETargetType targetType, Long targetId) {
        // בודקים מי המשתמש הנוכחי
        Long currentUserId = authService.getCurrentUserId();


        try {
            //אם הוא עשה כבר לייק --- אז לא תודה הרעיון הוא להוסיף לייק
            if (likeRepository.existsByUserIdAndTargetTypeAndTargetId(currentUserId, targetType, targetId)) {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            // אם לא יוצרים לו לייק
            //לתוכן הספציפי
            Like newLike = new Like(currentUserId, targetType, targetId);
            likeRepository.save(newLike);

            //אחרי שהוספנו סופרים שוב כמה לייקים יש
            //לתוכן הספציפי
            int newCount = likeRepository.countByTargetTypeAndTargetId(targetType, targetId);
            // מעדכנים את הכאונט
            //של התוכן הספציפי כמה לייקים יש לו
            updateContentCount(targetType, targetId, newCount);

            //בדקים של מי התוכן
            //כדי לשלוח לו התראה שעלה לו כאונט הלייקים
            Users contentOwner = getContentOwner(targetType, targetId);
            //אם מצאו למי זה שייך אז שולחים לו התראה
            if (contentOwner != null) {
                notificationService.handleLikeNotification(
                        targetType,
                        targetId,
                        contentOwner,
                        newCount
                );
            }

            return new ResponseEntity<>(newCount, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> removeLike(ETargetType targetType, Long targetId) {
        //מקבליםפ את היוזר
        Long currentUserId = authService.getCurrentUserId();

        try {
            Optional<Like> existingLike = likeRepository.findByUserIdAndTargetTypeAndTargetId(
                    currentUserId, targetType, targetId);

            existingLike.ifPresent(likeRepository::delete);

            int newCount = likeRepository.countByTargetTypeAndTargetId(targetType, targetId);
            updateContentCount(targetType, targetId, newCount);


            //בדקים של מי התוכן
            //כדי לשלוח לו התראה שעלה לו כאונט הלייקים
            Users contentOwner = getContentOwner(targetType, targetId);
            //אם מצאו למי זה שייך אז שולחים לו התראה
            if (contentOwner != null) {
                notificationService.handleLikeNotification(
                        targetType,
                        targetId,
                        contentOwner,
                        newCount
                );
            }

            return new ResponseEntity<>(newCount, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // FOLLOW *************************************************************

    @Transactional
    public ResponseEntity<EFollowStatus> toggleFollowRequest(Long targetUserId) {
        Users follower = authService.getCurrentUser();

        //רוצה לעקוב אחרי עצמו
        if (follower.getId().equals(targetUserId))
            return new ResponseEntity<>(EFollowStatus.NONE, HttpStatus.BAD_REQUEST);

        //בודקים אם כבר קיים מעקב
        Follow existingFollow = followRepository.findByFollowerIdAndFollowingIdForUpdate(
                follower.getId(), targetUserId);

        Users contentOwner = usersRepository.findUsersById(targetUserId);
        if (contentOwner == null) {
            return new ResponseEntity<>(EFollowStatus.NONE, HttpStatus.BAD_REQUEST);
        }
        //אם קיים מסירים מעקב
        if (existingFollow != null) {
            followRepository.delete(existingFollow);
            followRepository.flush();
            notificationService.handleUnfollowNotification(follower, contentOwner);
        }

        Follow newFollow = new Follow(follower.getId(), targetUserId, EFollowStatus.PENDING);
        followRepository.save(newFollow);
        followRepository.flush();

        notificationService.handleFollowRequestNotification(follower, contentOwner);

        return new ResponseEntity<>(EFollowStatus.PENDING, HttpStatus.ACCEPTED);
    }



    public ResponseEntity<EFollowStatus> getFollowStatus(Long targetUserId) {
        Users currentUser = authService.getCurrentUser();
//        if (currentUser == null)
//            return new ResponseEntity<>(EFollowStatus.NONE, HttpStatus.UNAUTHORIZED);

        // לבדוק אם תקיןןןן
        // עוקב אחרי עצמך
        if (currentUser.getId() == targetUserId)
            return new ResponseEntity<>(EFollowStatus.APPROVED, HttpStatus.OK);

        Follow follow = followRepository.findByFollowerIdAndFollowingId(
                currentUser.getId(), targetUserId);

        return new ResponseEntity<>(
                follow == null ? EFollowStatus.NONE : follow.getStatus(),
                HttpStatus.OK
        );
    }

    public ResponseEntity<?> approveFollow(Long followerId){
        return approveOrRejectFollow(followerId, ENotificationType.FOLLOW_REQUEST_ACCEPTED);
    }
    public ResponseEntity<?> RejectFollow(Long followerId){
        return approveOrRejectFollow(followerId, ENotificationType.FOLLOW_REQUEST_RECEIVED);
    }

    private ResponseEntity<?> approveOrRejectFollow(Long followerId, ENotificationType notify) {
        Users followingUser = authService.getCurrentUser();

        try {
            Optional<Follow> existingFollow = followRepository
                    .findByFollowerIdAndFollowingIdAndStatus(
                            followerId, followingUser.getId(), EFollowStatus.PENDING);

            Follow follow = existingFollow.orElseThrow(() ->
                    new NoSuchElementException("לא נמצאה בקשת מעקב ממתינה."));

            follow.setStatus(EFollowStatus.APPROVED);
            followRepository.save(follow);

            Users follower = usersRepository.findById(followerId).orElse(null);
            if (follower == null){
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            }

            notificationService.handleFollowRequestDecisions(follower, followingUser, notify);



            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }















    // FAVORITES *************************************************************

    @Transactional
    public ResponseEntity<NotificationSimpleDTO> addFavorite(ETargetType targetType, Long targetId) {
        Long currentUserId = authService.getCurrentUserId();

        try {
            if (!favoriteRepository.existsByUserIdAndTargetTypeAndTargetId(currentUserId, targetType, targetId)) {

                Favorite newFavorite = new Favorite(currentUserId, targetType, targetId);
                favoriteRepository.save(newFavorite);

                Users contentOwner = getContentOwner(targetType, targetId);
                if (contentOwner != null) {
                    Users currentUserEntity = authService.getCurrentUser();
                    notificationRepository.save(new Notification(
                            ENotificationType.FAVORITE_MUSIC,
                            contentOwner,
                            currentUserEntity,
                            targetType,
                            targetId
                    ));
                }
            }

            int newCount = favoriteRepository.countByTargetTypeAndTargetId(targetType, targetId);
            return new ResponseEntity<>(new NotificationSimpleDTO(targetId, newCount, false), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<NotificationSimpleDTO> removeFavorite(ETargetType targetType, Long targetId) {
        Long currentUserId = authService.getCurrentUserId();

        try {
            Optional<Favorite> existingFavorite = favoriteRepository.findByUserIdAndTargetTypeAndTargetId(
                    currentUserId, targetType, targetId);

            existingFavorite.ifPresent(favoriteRepository::delete);

            int newCount = favoriteRepository.countByTargetTypeAndTargetId(targetType, targetId);
            return new ResponseEntity<>(new NotificationSimpleDTO(targetId, newCount, false), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
