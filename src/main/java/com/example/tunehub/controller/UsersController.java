package com.example.tunehub.controller;

import com.example.tunehub.dto.UsersMusiciansDTO;
import com.example.tunehub.dto.UsersProfileDTO;
import com.example.tunehub.dto.UsersSignUpDTO;
import com.example.tunehub.dto.UsersUploadProfileImageDTO;
import com.example.tunehub.model.ERole;
import com.example.tunehub.model.EUserType;
import com.example.tunehub.model.Role;
import com.example.tunehub.model.Users;
import com.example.tunehub.security.CustomUserDetails;
import com.example.tunehub.security.jwt.JwtUtils;
import com.example.tunehub.service.FileUtils;
import com.example.tunehub.service.RoleRepository;
import com.example.tunehub.service.UsersMapper;
import com.example.tunehub.service.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "\\images\\";
    private UsersRepository usersRepository;
    private UsersMapper usersMapper;
    private RoleRepository roleRepository;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;


    @Autowired
    public UsersController(UsersRepository usersRepository, UsersMapper usersMapper, RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }


    //Get
    @GetMapping("/userById/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        try {
            Users u = usersRepository.findUsersById(id);
            if (u == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(u, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usersProfileImageDTOById/{id}")
    public ResponseEntity<UsersUploadProfileImageDTO> getUsersProfileImageDTOById(@PathVariable Long id) throws IOException {
        Users u = usersRepository.findUsersById(id);
        if (u != null) {
            return new ResponseEntity<>(usersMapper.usersToDTO(u), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/users")
    public ResponseEntity<List<Users>> getUsers() {
        try {
            List<Users> u = usersRepository.findAll();
            if (u == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(u, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usersByUserType/{user_type}")
    public ResponseEntity<List<Users>> getUserByUserType(@PathVariable EUserType user_type) {
        try {
            List<Users> u = usersRepository.findUsersByEUserType(user_type);
            if (u == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(u, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/usersByTeacherId/{teacher_id}")
    public ResponseEntity<List<Users>> getUsersByTeacher(@PathVariable Long teacher_id) {
        try {
            List<Users> u = usersRepository.findByTeacherId(teacher_id);
            if (u == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(u, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/userByName/{name}")
    public ResponseEntity<Users> getUserByName(@PathVariable String name) {
        try {
            Users u = usersRepository.findUsersByName(name);
            if (u == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(u, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("musicianById/{id}")
    public ResponseEntity<UsersMusiciansDTO> getMusicianById(@PathVariable Long id) {
        try {
            Users u = usersRepository.findUsersById(id);

            if (u == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(usersMapper.UsersToUsersMusiciansDTO(u), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("musicians")
    public ResponseEntity<List<UsersMusiciansDTO>> getMusicians() {
        try {
            List<Users> u = usersRepository.findAll();

            if (u == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(usersMapper.UsersToUsersMusiciansDTO(u), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Put
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<Users> signUpUser(@PathVariable Long id, @RequestBody Users user) {
        try {
            Users u = usersRepository.findUsersById(id);
            if (u == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            Users existingWithSameName = usersRepository.findUsersByName(user.getName());
            if (existingWithSameName != null && !existingWithSameName.getId().equals(id)) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

            u.setName(user.getName());
            u.setEmail(user.getEmail());
            u.setPassword(user.getPassword());
            u.setIsActive(user.isActive());
            u.setDescription(user.getDescription());
            u.setUserType(user.getUserType());
            //date-
            user.setEditedIn(LocalDate.now());

            Users updatedUser = usersRepository.save(u);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody Users u) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(u.getName(), u.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(Map.of("username", userDetails.getUsername()));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(
            // ⭐️ תיקון: הפוך את הקובץ לאופציונלי ב-Spring Boot
            @RequestPart(value = "image", required = false) MultipartFile file,

            @RequestPart("profile") UsersSignUpDTO user) {

        try {
            Users u = usersRepository.findByName(user.getName());
            if (u != null)
                return new ResponseEntity<>(HttpStatus.CONFLICT);

            // ⚠️ חשוב: עכשיו יש לבדוק אם הקובץ קיים לפני השימוש בו
            if (file != null && !file.isEmpty()) {
                String uniqueFileName = FileUtils.generateUniqueFileName(file);
                FileUtils.uploadImage(file,uniqueFileName);
                user.setImageProfilePath(uniqueFileName);
            } else {
                // אם אין קובץ, ודא שהנתיב לא נשאר NULL אם ה-DTO מצפה למחרוזת
                user.setImageProfilePath(null); // או נתיב ברירת מחדל
            }

            String pass = user.getPassword();
            user.setPassword(new BCryptPasswordEncoder().encode(pass));

            Users us = usersMapper.UsersSignUpDTOtoUsers(user);

            Role roleUser = roleRepository.findByRole(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            us.getRoles().add(roleUser);

            usersRepository.save(us);

            return new ResponseEntity<>(null, HttpStatus.CREATED);

        } catch (Exception e) {
            // במקרה של שגיאה, חזור 500
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/signOut")
    public ResponseEntity<?> signOut() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("you've been signed out!");
    }


    @PostMapping("/uploadImageProfile")
    public ResponseEntity<Users> uploadImageProfile(@RequestPart("image") MultipartFile file, @RequestPart("profile") Users p) {
        try {
            String uniqueFileName = FileUtils.generateUniqueFileName(file);
            FileUtils.uploadImage(file,uniqueFileName);
            p.setImageProfilePath(uniqueFileName);
//            Users users = usersMapper.UsersProfileDTOtoUsers();
            Users users = usersRepository.save(p);
            return new ResponseEntity<>(users, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Delete
    @DeleteMapping("/DeleteAllUser/{id}")
    public ResponseEntity DeleteAllUserById(@PathVariable Long id) {
        try {
            if (usersRepository.existsById(id)) {
                usersRepository.deleteById(id);
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @GetMapping("/get")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public String get() {
//        return "hello";
//    }

//   @GetMapping("/hotUsers")
//    public ResponseEntity<List<Users>> getHotUsers()   {
//        try{
//            List<Users> u = usersRepository.findTop10ByLikesOrderByLikesDesc();
//            //להוסיף לשקלול נוסיה של עוד בהמשך
//            if (u.isEmpty()) {
//                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//            }
//            return new ResponseEntity<>(u,HttpStatus.OK);
//        }
//        catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    // Heart - Beat
    private Long getCurrentUserId(Principal principal) {
        // יש להתאים את הפונקציה בהתאם ליישום ה-Security שלך (JWT/OAuth2)
        return Long.parseLong(principal.getName());
    }

    @PostMapping("/heartbeat")
    public ResponseEntity<?> sendHeartbeat(Principal principal) {
        try {
            // 1. קבל את ה-ID מהטוקן
            Long userId = getCurrentUserId(principal);

            // 2. עדכן את הסטטוס ואת חותמת הזמן
            usersRepository.findById(userId).ifPresent(user -> {
                user.setIsActive(true); // מגדיר את המשתמש כפעיל
                user.setLastActivityTimestamp(new Date()); // שעת הפעילות הנוכחית
                usersRepository.save(user);
            });

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            // חשוב לרשום את e.printStackTrace() ללוגים שלך כדי לדעת מה השתבש
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}





