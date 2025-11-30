package com.example.tunehub.controller;

import com.example.tunehub.dto.NotificationSimpleDTO;
import com.example.tunehub.model.EFollowStatus;
import com.example.tunehub.model.ETargetType;
import com.example.tunehub.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interaction")
public class InteractionController {

    private final InteractionService interactionService;

    @Autowired
    public InteractionController(InteractionService interactionService) {
        this.interactionService = interactionService;
    }

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
}
