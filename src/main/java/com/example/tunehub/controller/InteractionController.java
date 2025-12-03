package com.example.tunehub.controller;

import com.example.tunehub.dto.FavoriteItemDTO;
import com.example.tunehub.dto.NotificationSimpleDTO;
import com.example.tunehub.model.EFollowStatus;
import com.example.tunehub.model.ETargetType;
import com.example.tunehub.service.AuthService;
import com.example.tunehub.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/interaction")
public class InteractionController {

    private final InteractionService interactionService;
    private final AuthService authService;
    @Autowired
    public InteractionController(InteractionService interactionService,AuthService authService) {
        this.interactionService = interactionService;
        this.authService = authService;
    }


    // Likes
    @PostMapping("/like/add/{targetType}/{targetId}")
    public ResponseEntity<?> addLike(@PathVariable ETargetType targetType, @PathVariable Long targetId) {
        return interactionService.addLike(targetType, targetId);
    }

    @DeleteMapping("/like/remove/{targetType}/{targetId}")
    public ResponseEntity<?> removeLike(
            @PathVariable ETargetType targetType, @PathVariable Long targetId) {
        return interactionService.removeLike(targetType, targetId);
    }

    @PostMapping("/follow/toggle/{targetUserId}")
    public ResponseEntity<EFollowStatus> toggleFollowRequest(@PathVariable Long targetUserId) {
        return interactionService.toggleFollowRequest(targetUserId);
    }

    @GetMapping("/follow/status/{targetUserId}")
    public ResponseEntity<EFollowStatus> getFollowStatus(@PathVariable Long targetUserId) {
        return interactionService.getFollowStatus(targetUserId);
    }

    @PostMapping("/follow/approve/{followerId}")
    public ResponseEntity<?> approveFollow(@PathVariable Long followerId) {
        return interactionService.approveFollow(followerId);
    }

    @PostMapping("/favorite/add/{targetType}/{targetId}")
    public ResponseEntity<NotificationSimpleDTO> addFavorite(
            @PathVariable ETargetType targetType, @PathVariable Long targetId) {
        return interactionService.addFavorite(targetType, targetId);
    }

    @DeleteMapping("/favorite/remove/{targetType}/{targetId}")
    public ResponseEntity<NotificationSimpleDTO> removeFavorite(
            @PathVariable ETargetType targetType, @PathVariable Long targetId) {
        return interactionService.removeFavorite(targetType, targetId);
    }

    @GetMapping("/byType")
    public ResponseEntity<List<FavoriteItemDTO>> getFavoritesByType(
            @RequestParam("type") String typeString,
            @RequestParam(value = "search", required = false, defaultValue = "") String search
            //Principal principal
    ) {

//        if (principal == null) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }

        try {
            ETargetType targetType = ETargetType.valueOf(typeString.toUpperCase());
            Long currentUserId = authService.getCurrentUserId();
            List<FavoriteItemDTO> favorites = interactionService.getFavoritesForUserByType(
                    currentUserId,
                    targetType,
                    search
            );

            return new ResponseEntity<>(favorites, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            // טיפול במקרה שהמחרוזת type אינה Enum תקני (למשל, 'ABC')
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
