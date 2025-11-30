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
    private final NotificationRepository notificationRepository; // 💡 נניח שזה מספיק, אך שירות ייעודי עדיף

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
    // @Transactional - נחוץ אם פעולות ה-Repository מערבות יותר משורה אחת וצריכות להיות אטומיות
    @Transactional
    public ResponseEntity<?> updateRole(@PathVariable Long userId, @RequestBody RoleDTO roleDto) {

        // 1. קריאת שם הרול מה-DTO (משתמש בשדה 'name')
        String newRoleString = roleDto.getName();

        if (newRoleString == null || newRoleString.isEmpty()) {
            return ResponseEntity.badRequest().body("שדה הרול (name) חסר בגוף הבקשה.");
        }

        try {
            // 2. מציאת המשתמש
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("משתמש עם ID " + userId + " לא נמצא."));

            // 3. אימות והמרת הרול (נניח שיש לך ERole Enum)
            ERole targetRoleEnum;
            try {
                targetRoleEnum = ERole.valueOf(newRoleString);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("הרול " + newRoleString + " אינו רול חוקי.");
            }

            // 4. מציאת ישות הרול (Entity) מתוך ה-DB
            Role targetRole = roleRepository.findByName(targetRoleEnum)
                    .orElseThrow(() -> new IllegalArgumentException("Role Entity לא נמצא עבור " + newRoleString));

            // 5. עדכון הרולים: מחיקת הרולים הקיימים והוספת הרול החדש
            user.getRoles().clear();
            user.getRoles().add(targetRole);

            // 6. שמירה לדאטה בייס (ה-@Transactional יכול להפוך את השמירה לאוטומטית)
            usersRepository.save(user);

            String notificationMessage = "מזל טוב! קודמת בהצלחה לתפקיד: " + newRoleString.replace("ROLE_", ""); // הודעה נקייה יותר

            Notification notification = new Notification();
            notification.setUser(user); // משתמש היעד (שקיבל את הרול)
            notification.setMessage(notificationMessage);
            notification.setRead(false); // התראה חדשה היא תמיד לא נקראה

            // שמירת ההתראה החדשה ב-DB
            notificationRepository.save(notification);
            return ResponseEntity.ok().body("הרול עודכן בהצלחה ל: " + newRoleString);

        } catch (IllegalArgumentException e) {
            // טיפול בשגיאות שנזרקו בתוך הבלוק (משתמש/רול לא חוקיים)
            return ResponseEntity.badRequest().body("שגיאה בעדכון הרול: " + e.getMessage());
        } catch (Exception e) {
            // טיפול בשגיאות אחרות
            return ResponseEntity.internalServerError().body("שגיאה פנימית בשרת: " + e.getMessage());
        }
    }




}
