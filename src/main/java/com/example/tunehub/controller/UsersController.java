package com.example.tunehub.controller;

import com.example.tunehub.dto.*;
import com.example.tunehub.model.*;
import com.example.tunehub.security.CustomUserDetails;
import com.example.tunehub.security.jwt.JwtUtils;
import com.example.tunehub.service.UsersService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService usersService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public UsersController(UsersService usersService,
                           AuthenticationManager authenticationManager,
                           JwtUtils jwtUtils) {
        this.usersService = usersService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    // ---------- GET ----------
    @GetMapping("/userById/{id}")
    public ResponseEntity<UsersProfileFullDTO> getUserById(@PathVariable Long id) {
        return usersService.getUserById(id);
    }


    @GetMapping("/usersProfileImageDTOById/{id}")
    public ResponseEntity<UsersUploadProfileImageDTO> getUsersProfileImageDTOById(@PathVariable Long id) throws IOException {
        return usersService.getUsersProfileImageDTOById(id);
    }

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getUsers() {
        return usersService.getUsers();
    }

    @GetMapping("/usersByTeacherId/{teacher_id}")
    public ResponseEntity<List<Users>> getUsersByTeacher(@PathVariable Long teacher_id) {
        return usersService.getUsersByTeacher(teacher_id);
    }

    @GetMapping("/userByName/{name}")
    public ResponseEntity<Users> getUserByName(@PathVariable String name) {
        return usersService.getUserByName(name);
    }

    @GetMapping("/userByCity/{city}")
    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByCity(@PathVariable String city) {
        return usersService.getUsersByCity(city);
    }

    @GetMapping("/userByCountry/{country}")
    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByCountry(@PathVariable String country) {
        return usersService.getUsersByCountry(country);
    }

    @GetMapping("/userByCreatedAt/{createdAt}")
    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByCreatedAt(@PathVariable String createdAt) {
        return usersService.getUsersByCreatedAt(createdAt);
    }

    @GetMapping("musicianById/{id}")
    public ResponseEntity<UsersMusiciansDTO> getMusicianById(@PathVariable Long id) {
        return usersService.getMusicianById(id);
    }

    @GetMapping("/musicians")
    public ResponseEntity<List<UsersMusiciansDTO>> getMusicians() {
        return usersService.getMusicians();
    }

    @GetMapping("musiciansByName/{name}")
    public ResponseEntity<List<UsersMusiciansDTO>> getMusiciansByName(@PathVariable String name) {
        return usersService.getMusiciansByName(name);
    }

    @GetMapping("/usersByUserType")
    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByUserType(
            @RequestParam List<EUserType> userTypes) {
        return usersService.getUsersByUserType(userTypes);
    }

    // ---------- PUT ----------
    @PutMapping("update-user-type/{userId}/{newType}")
    public ResponseEntity<UsersMusiciansDTO> updateUserType(@PathVariable Long userId, @PathVariable String newType) {
        return usersService.updateUserType(userId, newType);
    }

    @PutMapping("/joinTeacher/{studentId}/{teacherId}")
    public ResponseEntity<?> joinTeacher(@PathVariable Long studentId, @PathVariable Long teacherId) {
        return usersService.joinTeacher(studentId, teacherId);
    }

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
            @RequestPart(value = "image", required = false) MultipartFile file) throws IOException {

        return usersService.updateUser(id, name, email, city, country, description, isActive, userTypes, imageProfilePath, file);
    }

    // ---------- POST ----------
    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@Valid @RequestBody UsersLogInDTO u) {
        return usersService.signIn(u, authenticationManager, jwtUtils);
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(
            @Valid @RequestPart(value = "image", required = false) MultipartFile file,
            @RequestPart("profile") UsersSignUpDTO user) throws IOException {
        return usersService.signUp(user, file);
    }

    @PostMapping("/signOut")
    public ResponseEntity<?> signOut() {
        return usersService.signOut(jwtUtils);
    }

    @PostMapping("/uploadImageProfile")
    public ResponseEntity<Users> uploadImageProfile(@Valid @RequestPart("image") MultipartFile file, @RequestPart("profile") Users p) throws IOException {
        return usersService.uploadImageProfile(file, p);
    }

    @PostMapping("/heartbeat")
    public ResponseEntity<?> sendHeartbeat(Principal principal) {
        return usersService.sendHeartbeat(principal);
    }

    @PostMapping("/chat")
    public String getResponse(@RequestBody ChatRequest chatRequest){
        return usersService.getResponse(chatRequest);
    }

    @PostMapping("/signupTeacher/{id}")
    @Transactional
    public ResponseEntity<?> signUpAsTeacher(@Valid @PathVariable Long id,
                                             @Valid @RequestBody TeacherSignUpDTO teacherDetails) {
        return usersService.signUpAsTeacher(id, teacherDetails);
    }

    // ---------- DELETE ----------
    @DeleteMapping("delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        return usersService.deleteUser(userId);
    }
}

