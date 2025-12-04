package com.example.tunehub.controller;

import com.example.tunehub.dto.RoleDTO;
import com.example.tunehub.model.ERole;
import com.example.tunehub.model.Notification;
import com.example.tunehub.model.Role;
import com.example.tunehub.model.Users;
import com.example.tunehub.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final UsersRepository usersRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public RoleController(RoleRepository roleRepository,RoleMapper roleMapper,UsersRepository usersRepository ,NotificationRepository notificationRepository)
    {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.usersRepository = usersRepository;
        this.notificationRepository= notificationRepository;
    }

    @PutMapping("/admin/{userId}/role")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    @Transactional
    public ResponseEntity<?> updateRole(@PathVariable Long userId, @RequestBody RoleDTO roleDto) {

        String newRoleString = roleDto.getName();

        if (newRoleString == null || newRoleString.isEmpty()) {
            return ResponseEntity.badRequest().body("The role field is missing in the request body.");
        }

        try {
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("UserId  " + userId + "not found."));

            ERole targetRoleEnum;
            try {
                targetRoleEnum = ERole.valueOf(newRoleString);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("role " + newRoleString + " no legal .");
            }

            Role targetRole = roleRepository.findByName(targetRoleEnum)
                    .orElseThrow(() -> new IllegalArgumentException("Role Entity לא נמצא עבור " + newRoleString));

            user.getRoles().clear();
            user.getRoles().add(targetRole);

            usersRepository.save(user);

            String notificationMessage = "מזל טוב! קודמת בהצלחה לתפקיד: " + newRoleString.replace("ROLE_", ""); // הודעה נקייה יותר

            Notification notification = new Notification();
            notification.setUser(user); // משתמש היעד (שקיבל את הרול)
            notification.setMessage(notificationMessage);
            notification.setRead(false); // התראה חדשה היא תמיד לא נקראה

            notificationRepository.save(notification);
            return ResponseEntity.ok().body("Role status " + newRoleString);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Role error " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("error in the network " + e.getMessage());
        }
    }




}
