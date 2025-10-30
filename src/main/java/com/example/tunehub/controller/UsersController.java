package com.example.tunehub.controller;

import com.example.tunehub.dto.UsersUploadProfileImageDTO;
import com.example.tunehub.model.UserType;
import com.example.tunehub.model.Users;
import com.example.tunehub.service.FileUtils;
import com.example.tunehub.service.UsersMapper;
import com.example.tunehub.service.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UsersController {

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "\\images\\";
    private UsersRepository usersRepository;
    private UsersMapper usersMapper;

    @Autowired
    public UsersController(UsersRepository usersRepository, UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
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

    @GetMapping("/usersProfileImageDTO/{id}")
    public ResponseEntity<UsersUploadProfileImageDTO> getUsersProfileImageDTO(@PathVariable Long id) throws IOException {
        Users u = usersRepository.findUsersById(id);
        if (u != null) {
            return new ResponseEntity<>(usersMapper.usersToDTO(u), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    //    @GetMapping("/hotUsers")
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
    public ResponseEntity<List<Users>> getUserByUserType(@PathVariable UserType user_type) {
        try {
            List<Users> u = usersRepository.findUsersByUserType(user_type);
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


    //אין משתמשים באותו שם -- איך עושים חיפוש קטוע שמתחילים לרשום את השם וכבר קופצים פרופילים
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
            u.setActive(user.isActive());
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

    //Post
    @PostMapping("/signUp")
    public ResponseEntity<Users> signUpUser(@RequestBody Users user) {
        try {
            Users existingUser = usersRepository.findUsersByName(user.getName());
            if (existingUser != null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            user.setCreatedAt(LocalDate.now());
            user.setEditedIn(null);//האם מיותר ?
            Users savedUser = usersRepository.save(user);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logIn")
    public ResponseEntity<Users> logIn(@RequestBody Users u) {
        try {
            Users u1 = usersRepository.findUsersByName(u.getName());
            if (u1 == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            if (!u1.getPassword().equals((u.getPassword()))) {  //בדיקה לפי סיסמא
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }

            // שכחתי סיסמא
            return new ResponseEntity<>(u1, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/uploadImageProfile")
    public ResponseEntity<Users> uploadImageProfile(@RequestPart("image") MultipartFile file, @RequestPart("profile") Users p) {
        try {
            FileUtils.uploadImage(file);
            p.setImagePath(file.getOriginalFilename());
            Users users = usersRepository.save(p);
            return new ResponseEntity<>(users, HttpStatus.CREATED);
        } catch (IOException e) {
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


}

