package com.example.tunehub.controller;

import com.example.tunehub.dto.UsersMusiciansDTO;
import com.example.tunehub.model.Role;
import com.example.tunehub.model.Teacher;
import com.example.tunehub.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    private TeacherRepository teacherRepository;
    private TeacherMapper teacherMapper;
    private RoleRepository roleRepository;
    private InstrumentRepository instrumentRepository;
    private UsersRepository usersRepository;


    @Autowired
    public TeacherController(TeacherRepository teacherRepository, TeacherMapper teacherMapper,
                              UsersRepository usersRepository,InstrumentRepository instrumentRepository,
                             RoleRepository roleRepository) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
        this.usersRepository= usersRepository;
        this.instrumentRepository= instrumentRepository;
        this.roleRepository = roleRepository;
    }

    //Get
    @GetMapping("/teacherById/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        try {
            Teacher t = teacherRepository.findTeacherById(id);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(t, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<Teacher>> getTeachers() {
        try {
            List<Teacher> t = teacherRepository.findAll();
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(t, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/teachersByName/{name}")
    public ResponseEntity<List<Teacher>> getTeachersByName(@PathVariable String name) {
        try {
            List<Teacher> t = teacherRepository.findAllByName(name);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(t, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/teachersByCity/{city}")
    public ResponseEntity<List<Teacher>> getTeachersByCity(@PathVariable String city) {
        try {
            List<Teacher> t = teacherRepository.findAllByCity(city);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(t, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/teachersByCountry/{country}")
    public ResponseEntity<List<Teacher>> getTeachersByCountry(@PathVariable String country) {
        try {
            List<Teacher> t = teacherRepository.findAllByCountry(country);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(t, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/teachersByLessonDuration/{lessonDuration}")
    public ResponseEntity<List<Teacher>> getTeachersByLessonDuration(@PathVariable Double lessonDuration) {
        try {
            List<Teacher> t = teacherRepository.findAllByLessonDuration(lessonDuration);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(t, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/teachersByExperience/{experience}")
    public ResponseEntity<List<Teacher>> getTeachersByExperience(@PathVariable int experience) {
        try {
            List<Teacher> t = teacherRepository.findAllByExperience(experience);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(t, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/teachersByInstrumentId/{instrument_id}")
    public ResponseEntity<List<Teacher>> getTeachersByInstrumentId(@PathVariable Long instrument_id) {
        try {
            List<Teacher> t = teacherRepository.findAllByInstrumentsId(instrument_id);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(t, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/teacherById/{id}")
    public ResponseEntity DeleteTeacherById(@PathVariable Long id){
        try{
            if(teacherRepository.existsById(id)){
                teacherRepository.deleteById(id);
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping("/signup/{userId}")
//    public ResponseEntity<?> signUpAsTeacher(@PathVariable Long userId,
//                                             @RequestBody TeacherSignUpDTO teacherDetails) {
//        try {
//            // הוספת לוגיקה לאימות והמרת המשתמש
//            // ...
//            // userService.upgradeToTeacher(userId, teacherDetails);
//            // ...
//            return ResponseEntity.ok("User upgraded to teacher successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
//
//
//    @PreAuthorize("isAuthenticated() and #id == authentication.principal.id")
//    @PostMapping("/signup/{id}")
//    @Transactional // ✅ חשוב מאוד! ודאי שטרנזקציה עוטפת את כל פעולות המחיקה/שמירה
//    public ResponseEntity<?> signUpAsTeacher(@PathVariable Long id,
//                                             @RequestBody TeacherSignUpDTO teacherDetails) {
//        try {
//            // 1. מצא את המשתמש הקיים
//            Users user = usersRepository.findById(id)
//                    .orElseThrow(() -> new UserValidationException("User not found"));
//
//            // 2. אימות זכאות (כפי שדרשת)
//            if (user.getCity() == null || user.getCity().isEmpty() ||
//                    user.getCountry() == null || user.getCountry().isEmpty() ||
//                    user.getDescription() == null || user.getDescription().isEmpty()) {
//                throw new UserValidationException("City, Country, and Description must be filled before signing up as a teacher.");
//            }
//
//            // 3. יצירת אובייקט Teacher חדש
//            Teacher teacher = new Teacher();
//            // העתקת כל הנתונים משדות Users ל-Teacher
//            BeanUtils.copyProperties(user, teacher, "id"); // העתקת שדות, לא כולל ID
//
//            // 4. הגדרת השדות הייחודיים למורה
//            teacher.setPricePerLesson(teacherDetails.getPricePerLesson());
//            teacher.setExperience(teacherDetails.getExperience());
//            teacher.setLessonDuration(teacherDetails.getLessonDuration());
//            teacher.setEUserType(EUserType.TEACHER); // עדכון הטיפוס
//
//            // 5. מציאת כלי הנגינה ושיוכם
//            List<Instrument> instruments = instrumentRepository.findAllById(teacherDetails.getInstrumentsIds());
//            teacher.setInstruments(instruments);
//
//            // 6. הוספת תפקיד 'מורה'
//            Role teacherRole = roleRepository.findByName(ERole.ROLE_TEACHER)
//                    .orElseThrow(() -> new RuntimeException("Teacher role not found."));
//            teacher.getRoles().add(teacherRole);
//
//            // 7. מחיקת המשתמש הישן ושמירת המורה החדש
//            // מכיוון שיש ירושה (JOINED) ו-Teacher יורש מ-Users, ייתכן שתצטרכי למחוק את ה-Users
//            // ואז לשמור את ה-Teacher. פעולה זו תלויה באסטרטגיית ה-JPA המדויקת שלך.
//            // אם ה-ID נשמר (כפי שקורה ב-JOINED), צריך לדאוג לשמור את המורה באותו ID.
//
//            // לשם פשטות והנחה על ירושה: מחיקת ישן ושמירת חדש
//            usersRepository.delete(user);
//            teacher.setId(id); // נשתמש באותו ID כדי לשמור על קשרים קיימים
//            Teacher upgradedTeacher = teacherRepository.save(teacher);
//
//            // 8. החזרת DTO
//            TeacherResponseDTO responseDTO = teacherMapper.teacherToTeacherResponseDTO(upgradedTeacher);
//
//            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
//        } catch (UserValidationException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return new ResponseEntity<>("Failed to upgrade user to teacher: " + e.getMessage(),
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//}
    // נקודת קצה לרשימת המורים (לדף החיפוש)
//    @GetMapping
//    public ResponseEntity<List<UsersMusiciansDTO>> getAllTeachers() {
//        // רשימה שמציגה רק מורים
//        List<UsersMusiciansDTO> teachers = usersService.getTeachersList();
//        return ResponseEntity.ok(teachers);
//    }


}
