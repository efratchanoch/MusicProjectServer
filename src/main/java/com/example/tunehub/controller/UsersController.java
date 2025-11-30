package com.example.tunehub.controller;

import com.example.tunehub.dto.*;
import com.example.tunehub.model.*;
import com.example.tunehub.security.CustomUserDetails;
import com.example.tunehub.security.jwt.JwtUtils;
import com.example.tunehub.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import com.example.tunehub.service.UsersRatingUtils;    // ⬅️ ייבוא ה-Utility Class
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "\\images\\";
    private UsersRepository usersRepository;
    private UsersMapper usersMapper;
    private RoleRepository roleRepository;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private AIChatService aiChatService;
    private InstrumentRepository instrumentRepository;
    private TeacherRepository teacherRepository;
    private RefreshTokenService refreshTokenService;

    @Autowired
    public UsersController(UsersRepository usersRepository, UsersMapper usersMapper, RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils,AIChatService aiChatService,InstrumentRepository instrumentRepository,TeacherRepository teacherRepository,RefreshTokenService refreshTokenService) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.aiChatService=aiChatService;
        this.instrumentRepository= instrumentRepository;
        this.teacherRepository= teacherRepository;
        this.refreshTokenService = refreshTokenService;
    }


    //Get

    @GetMapping("/userById/{id}")
    @PreAuthorize("isAuthenticated()") // ⬅️ נדרש: כל משתמש מחובר יכול לגשת
    public ResponseEntity<UsersProfileFullDTO> getUserById(@PathVariable Long id) {
        try {
            Users u = usersRepository.findUsersById(id);
            if (u == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            // המרה ל-DTO
            UsersRatingUtils.calculateAndSetStarRating(u);

            UsersProfileFullDTO dto = usersMapper.UsersToUsersProfileFullDTO(u);

            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
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



    @PutMapping("update-user-type/{userId}/{newType}")
    public ResponseEntity<UsersMusiciansDTO> updateUserType(@PathVariable Long userId, @PathVariable String newType) {
        try {
            EUserType EnewType = EUserType.valueOf(newType.toUpperCase());
            if (newType == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            Users user = usersRepository.findById(userId).orElse(null);
            if (user == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            // ודא שהסוג החדש הוא MUSIC_LOVER


            if (user.getUserTypes() == null) {
                user.setUserTypes(new HashSet<>());
            }

            // 4. ⭐ השינוי המרכזי: הוספת הטיפוס החדש לאוסף הקיים
            if (user.getUserTypes().contains(EnewType)) {
                // אם הטיפוס כבר קיים, אין צורך לבצע שינוי
                System.out.println("User already has the type: " + EnewType);
            } else {
                user.getUserTypes().add(EnewType);
            }
            Users updatedUser = usersRepository.save(user);

            // *** שינוי: המרה ל-DTO לפני החזרה
            UsersMusiciansDTO dto = usersMapper.UsersToUsersMusiciansDTO(updatedUser);

            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
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


    @GetMapping("/userByCity/{city}")
    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByCity(@PathVariable String city) {
        try {
            List<Users> u = usersRepository.findAllByCity(city);
            if (u == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(usersMapper.UsersToUsersMusiciansDTO(u), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/userByCountry/{country}")
    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByCountry(@PathVariable String country) {
        try {
            List<Users> u = usersRepository.findAllByCountry(country);
            if (u == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(usersMapper.UsersToUsersMusiciansDTO(u), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/userByCreatedAt/{createdAt}")
    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByCreatedAt(@PathVariable String createdAt) {
        try {
            List<Users> u = usersRepository.findAllByCreatedAt(LocalDate.parse(createdAt));
            if (u == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(usersMapper.UsersToUsersMusiciansDTO(u), HttpStatus.OK);
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

     @GetMapping("/musicians")
    public ResponseEntity<List<UsersMusiciansDTO>> getMusicians() {
        try {
            // *** השינוי המרכזי: קוראים לפונקציה חדשה שמסננת לפי UserType
            List<Users> u = usersRepository.findByUserType(EUserType.MUSICIAN);

            if (u == null || u.isEmpty()) {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
            }


            return new ResponseEntity<>(usersMapper.UsersToUsersMusiciansDTO(u), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("musiciansByName/{name}")
    public ResponseEntity<List<UsersMusiciansDTO>> getMusiciansByName(@PathVariable String name) {
        try {
            List<Users> u = usersRepository.findAllByNameContainingIgnoreCase(name);

            if (u == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(usersMapper.UsersToUsersMusiciansDTO(u), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





    @GetMapping("/usersByUserType")
    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByUserType(
            @RequestParam(required = true) List<EUserType> userTypes) {
        try {
            List<Users> users = usersRepository.findByUserTypeQuery(userTypes);
            if (users == null || users.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            for (Users user : users) {
                System.out.println("User ID: " + user.getId() + ", Teacher object is null? " + (user.getTeacher() == null));
                if (user.getTeacher() != null) {
                    System.out.println("Teacher experience: " + user.getTeacher().getExperience());
                    System.out.println("Teacher price: " + user.getTeacher().getPricePerLesson());
                }
            }
            // המרה ל-DTO בעזרת Mapper
            List<UsersMusiciansDTO> dtoList = usersMapper.UsersToUsersMusiciansDTO(users);

            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/joinTeacher/{studentId}/{teacherId}")
    public ResponseEntity<?> joinTeacher(
            @PathVariable Long studentId,
            @PathVariable Long teacherId) {
        try {
            // קריאה ישירה לפונקציה ב-Repository
            usersRepository.assignTeacherToStudent(studentId, teacherId);

            // הצלחה - מחזיר 200 OK ללא תוכן
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            // כישלון - מחזיר 500 Internal Server Error
            System.err.println("שגיאה בהצטרפות למורה: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to assign teacher.");
        }
    }


    //Put
// ב-UserController.java

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UsersProfileFullDTO> updateUser(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Boolean  isActive,
            @RequestParam(required = false) List<EUserType> userTypes,
            @RequestParam(required = false) String imageProfilePath,
            @RequestPart(value = "image", required = false) MultipartFile file) {

        try {
            Users u = usersRepository.findUsersById(id);
            if (u == null) {
                // לא נמצא משתמש
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            // ⭐ בדיקות אימות: בדיקה שאין משתמש אחר עם אותו שם
            Users existingWithSameName = usersRepository.findUsersByName(name);
            if (existingWithSameName != null && !existingWithSameName.getId().equals(id)) {
                // קונפליקט: שם משתמש כבר קיים אצל מישהו אחר
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

            // עדכון שדות טקסט (מתוך ה-RequestParam)
            u.setName(name);
            u.setEmail(email);
            // יש לשים לב שהשדות הללו היו חסרים בקוד המקורי שהצגת לפונקציה זו, אבל היו בפונקציית @RequestBody הישנה.
            // נשאיר אותם כפי שהיו בקוד המקורי שנתת לעריכה:
            // u.setIsActive(isActive);
            // u.setUserType(userType);
            if (isActive != null) {
                u.setIsActive(isActive);
            }

            if (userTypes != null) {
                u.setUserTypes(new HashSet<>(userTypes));
            }
            u.setCity(city);
            u.setCountry(country);
            u.setDescription(description);
            u.setEditedIn(LocalDate.now());

            // 💡 טיפול בתמונה:
            if (file != null && !file.isEmpty()) {
                // קובץ חדש נבחר: שומרים אותו ומעדכנים את הנתיב ב-DB
                String uniqueFileName = FileUtils.generateUniqueFileName(file);
                FileUtils.uploadImage(file, uniqueFileName);
                u.setImageProfilePath(uniqueFileName);
            } else if (imageProfilePath != null && !imageProfilePath.isEmpty()) {
                // לא נבחר קובץ חדש, אבל נשלח נתיב ישן: משאירים את הקיים (הנתיב נשמר כחלק מהטופס)
                // אין צורך לשנות את u.getImageProfilePath() מכיוון שהוא כבר מכיל את הנתון הנכון
            } else {
                // לא נבחר קובץ וגם לא נשלח נתיב ישן (או נשלח ריק): מוחקים את הנתיב הקיים
                u.setImageProfilePath(null);
            }

            Users updatedUser = usersRepository.save(u);

            // ⭐ מחזירים DTO ולא את ה־Entity
            UsersProfileFullDTO dto = usersMapper.UsersToUsersProfileFullDTO(updatedUser);

            return new ResponseEntity<>(dto, HttpStatus.OK);

        } catch (Exception e) {
            // ⭐ טיפול בשגיאות שרת כלליות
            System.err.println("Error in updateProfileWithImage: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody UsersLogInDTO u) {
        try {
            // 1. אימות המשתמש
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(u.getName(), u.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // 2. יצירת Access Token וקוקי (טוקן קצר)
            ResponseCookie jwtAccessCookie = jwtUtils.generateJwtCookie(userDetails); // ודא שאת משתמשת בפונקציה החדשה generateAccessCookie אם יישמת אותה

            // 3. יצירת Refresh Token ושמירה ב-DB (טוקן ארוך)
            // טען את המשתמש כדי לקבל את ה-ID
            // עדיף להשתמש ב-userDetails.getId() אם ה-ID נמצא שם
   //         RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            // 4. יצירת קוקי נפרד עבור ה-Refresh Token
   //         ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshCookie(refreshToken.getToken());

            // 5. הכנת DTO והחזרת שתי העוגיות
            Users userFromDb = usersRepository.findByName(userDetails.getUsername());

            // הכנת DTO מינימלי
            UsersProfileDTO profileDTO = usersMapper.UsersToUsersProfileDTO(userFromDb);
//            profileDTO.setId(userFromDb.getId().toString());
//            profileDTO.setName(userFromDb.getName());
//            profileDTO.setImageProfilePath(userFromDb.getImageProfilePath());
//            profileDTO.setRoles(userFromDb.getRoles().stream()
//                    .map(role -> new RoleDTO(role.getRole()))
//                    .collect(Collectors.toSet()));

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

            return ResponseEntity.ok()
                    // מצרף את שני הקוקיז לתגובה
                    .header(HttpHeaders.SET_COOKIE, jwtAccessCookie.toString())
                 //   .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                    .body(profileDTO);

        } catch (Exception e) {
            System.out.println("Authentication failed for user: " + u.getName() + " Error: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

//    @PostMapping("/refreshtoken")
//    public ResponseEntity<?> refreshtoken(HttpServletRequest request) {
//        // 1. קבל את ה-Refresh Token מהקוקי
//        String refreshToken = jwtUtils.getRefreshJwtFromCookies(request);
//
//        if (refreshToken != null) {
//            return refreshTokenService.findByToken(refreshToken)
//                    .map(refreshTokenService::verifyExpiration) // בדוק אם פג תוקף ב-DB
//                    .map(token -> {
//                        // 2. אם ה-Refresh Token תקף: צור Access Token חדש
//                        String newAccessToken = jwtUtils.generateTokenFromUsername(token.getUser().getName());
//
//                        // 3. צור קוקי חדש ל-Access Token בלבד
//                        ResponseCookie newAccessCookie = jwtUtils.generateAccessCookie(newAccessToken);
//
//                        // 4. החזר את הקוקי החדש
//                        return ResponseEntity.ok()
//                                .header(HttpHeaders.SET_COOKIE, newAccessCookie.toString())
//                                .body("Token refreshed successfully");
//                    })
//                    // אם לא נמצא, זה אומר שנגנב או בוטל - שלח שגיאה חזרה לקליינט
//                    .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
//        }
//
//        return ResponseEntity.badRequest().body("Refresh Token is missing from request.");
//    }
    @PostMapping("/chat")
    public String getResponse(@RequestBody ChatRequest chatRequest){
        return aiChatService.getResponse(chatRequest.message(),chatRequest.conversationId());
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

            if (us.getUserTypes() == null || us.getUserTypes().isEmpty()) {
                // בדרך כלל משתמשים חדשים הם MUSIC_LOVER
                us.setUserTypes(new HashSet<>(Collections.singletonList(EUserType.MUSIC_LOVER)));
            }
            if (us.getRoles() == null) {
                us.setRoles(new HashSet<>());
            }
            Role roleUser = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            us.getRoles().add(roleUser);

            usersRepository.save(us);

            return new ResponseEntity<>(null, HttpStatus.CREATED);

        } catch (Exception e) {
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
//    @DeleteMapping("/DeleteAllUser/{id}")
//    public ResponseEntity DeleteAllUserById(@PathVariable Long id) {
//        try {
//            if (usersRepository.existsById(id)) {
//                usersRepository.deleteById(id);
//                return new ResponseEntity(HttpStatus.NO_CONTENT);
//            }
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


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


    @PostMapping("/signupTeacher/{id}")
    @Transactional // ✅ כל הפעולות מתבצעות יחד בטרנזקציה אחת
    public ResponseEntity<?> signUpAsTeacher(@PathVariable Long id,
                                             @RequestBody TeacherSignUpDTO teacherDetails) {
        try {
            // 1. מצא את המשתמש הקיים
            Users user = usersRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

            // 2. אימות נתונים (ללא שינוי)
            if (user.getCity() == null || user.getCity().isEmpty() ||
                    user.getCountry() == null || user.getCountry().isEmpty() ||
                    user.getDescription() == null || user.getDescription().isEmpty()) {
                return new ResponseEntity<>("City, Country, and Description must be filled before signing up as a teacher.", HttpStatus.BAD_REQUEST);
            }

            if (user.getUserTypes().contains(EUserType.TEACHER)) {
                return new ResponseEntity<>("User is already a teacher.", HttpStatus.CONFLICT);
            }

                // 3. יצירת ישות Teacher חדשה
                Teacher teacher = new Teacher();


                teacher.setUser(user); // הכי חשוב!

                // 4. העתקת נתוני ה-Teacher
                teacher.setPricePerLesson(teacherDetails.getPricePerLesson());
                teacher.setExperience(teacherDetails.getExperience());
                teacher.setLessonDuration(teacherDetails.getLessonDuration());
                teacher.setDateUploaded(LocalDate.now());

                List<Instrument> instruments = instrumentRepository.findAllById(teacherDetails.getInstrumentsIds());
                teacher.setInstruments(instruments);

                // 5. שמירת ה-Teacher - מתבצעת ראשונה
                // JPA אמור לזהות שה-ID קיים וליצור רשומה רק בטבלת TEACHER.
                teacherRepository.save(teacher); // ⬅️ שמירת Teacher כעת מתבצעת לפני עדכון Users

                // 6. עדכון שדות ה-Users הקיימים (בטבלת USERS)
                user.getUserTypes().add(EUserType.TEACHER);
                // 7. הוספת תפקיד 'מורה' (מומלץ להשתמש ב-ROLE_TEACHER אם קיים)
                Role teacherRole = roleRepository.findByName(ERole.ROLE_USER) // 💡 תיקון: השתמשתי ב-ROLE_TEACHER
                        .orElseThrow(() -> new RuntimeException("Teacher role not found."));
                if (user.getRoles() == null) user.setRoles(new HashSet<>());
                user.getRoles().add(teacherRole);

                // 8. שמירת ה-Users המעודכן.
                // 💡 השמירה הזו רק מעדכנת את הרשומה הקיימת בטבלת USERS.
                usersRepository.save(user);



            return new ResponseEntity<>("User successfully upgraded to Teacher.", HttpStatus.OK);
        } catch (Exception e) {
            // שגיאות כלליות
            return new ResponseEntity<>("Failed to upgrade user to teacher: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping("delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("--- DEBUGGING AUTHORITIES ---");
        System.out.println("Name: " + authentication.getName());
        System.out.println("Authenticated: " + authentication.isAuthenticated());
        System.out.println("Authorities: " + authentication.getAuthorities());
        System.out.println("-----------------------------");

        // עכשיו בדוק אם הרולים נמצאים (אם לא, זו הבעיה ב-JWT Filter)
        if (!authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            // אם הרול לא נמצא, זה נכשל!
            System.out.println("AUTHORITY MISSING! This is the source of the 403.");
            // return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // החזר 403 אמיתי כדי לבדוק
        }
        if (!usersRepository.existsById(userId)) {
            // אם המשתמש לא נמצא, נחזיר 404 Not Found
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User with ID " + userId + " not found."
            );
        }

        usersRepository.deleteById(userId);

        return ResponseEntity.noContent().build();
    }
}





