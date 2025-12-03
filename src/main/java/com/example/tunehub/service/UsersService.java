package com.example.tunehub.service;

import com.example.tunehub.dto.*;
import com.example.tunehub.model.*;
import com.example.tunehub.security.CustomUserDetails;
import com.example.tunehub.security.jwt.JwtUtils;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    private final RoleRepository roleRepository;
    private final AIChatService aiChatService;
    private final InstrumentRepository instrumentRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository,
                        UsersMapper usersMapper,
                        RoleRepository roleRepository,
                        AIChatService aiChatService,
                        InstrumentRepository instrumentRepository,
                        TeacherRepository teacherRepository) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
        this.roleRepository = roleRepository;
        this.aiChatService = aiChatService;
        this.instrumentRepository = instrumentRepository;
        this.teacherRepository = teacherRepository;
    }

    // Get
    public ResponseEntity<UsersProfileFullDTO> getUserById(Long id) {
        Users u = usersRepository.findUsersById(id);
        if (u == null) return ResponseEntity.notFound().build();

        UsersRatingUtils.calculateAndSetStarRating(u);
        UsersProfileFullDTO dto = usersMapper.UsersToUsersProfileFullDTO(u);

        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<UsersUploadProfileImageDTO> getUsersProfileImageDTOById(Long id) throws IOException {
        Users u = usersRepository.findUsersById(id);
        if (u != null) {
            UsersUploadProfileImageDTO dto = usersMapper.usersToDTO(u);
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }


    public ResponseEntity<List<Users>> getUsers() {
        List<Users> u = usersRepository.findAll();
        return ResponseEntity.ok(u);
    }

    public ResponseEntity<List<Users>> getUsersByTeacher(Long teacherId) {
        List<Users> u = usersRepository.findByTeacherId(teacherId);
        return ResponseEntity.ok(u);
    }

    public ResponseEntity<Users> getUserByName(String name) {
        Users u = usersRepository.findUsersByName(name);
        if (u == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(u);
    }

    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByCity(String city) {
        List<Users> u = usersRepository.findAllByCity(city);
        return ResponseEntity.ok(usersMapper.UsersToUsersMusiciansDTO(u));
    }

    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByCountry(String country) {
        List<Users> u = usersRepository.findAllByCountry(country);
        return ResponseEntity.ok(usersMapper.UsersToUsersMusiciansDTO(u));
    }

    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByCreatedAt(String createdAt) {
        List<Users> u = usersRepository.findAllByCreatedAt(LocalDate.parse(createdAt));
        return ResponseEntity.ok(usersMapper.UsersToUsersMusiciansDTO(u));
    }

    public ResponseEntity<UsersMusiciansDTO> getMusicianById(Long id) {
        Users u = usersRepository.findUsersById(id);
        if (u == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(usersMapper.UsersToUsersMusiciansDTO(u));
    }

    public ResponseEntity<List<UsersMusiciansDTO>> getMusicians() {
        List<Users> u = usersRepository.findByUserType(EUserType.MUSICIAN);
        return ResponseEntity.ok(usersMapper.UsersToUsersMusiciansDTO(u));
    }

    public ResponseEntity<List<UsersMusiciansDTO>> getMusiciansByName(String name) {
        List<Users> u = usersRepository.findAllByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(usersMapper.UsersToUsersMusiciansDTO(u));
    }

    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByUserType(List<EUserType> userTypes) {
        List<Users> users = usersRepository.findByUserTypeQuery(userTypes);
        return ResponseEntity.ok(usersMapper.UsersToUsersMusiciansDTO(users));
    }

    // Put
    public ResponseEntity<UsersMusiciansDTO> updateUserType(Long userId, String newType) {
        EUserType EnewType = EUserType.valueOf(newType.toUpperCase());
        Users user = usersRepository.findById(userId).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();

        if (user.getUserTypes() == null) user.setUserTypes(new HashSet<>());
        if (!user.getUserTypes().contains(EnewType)) user.getUserTypes().add(EnewType);

        Users updatedUser = usersRepository.save(user);
        return ResponseEntity.ok(usersMapper.UsersToUsersMusiciansDTO(updatedUser));
    }

    public ResponseEntity<?> joinTeacher(Long studentId, Long teacherId) {
        usersRepository.assignTeacherToStudent(studentId, teacherId);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<UsersProfileFullDTO> updateUser(Long id, String name, String email,
                                                          String city, String country, String description,
                                                          Boolean isActive, List<EUserType> userTypes,
                                                          String imageProfilePath, MultipartFile file) throws IOException {
        Users u = usersRepository.findUsersById(id);
        if (u == null) return ResponseEntity.notFound().build();

        String originalPasswordHash = u.getPassword();
        Users existingWithSameName = usersRepository.findUsersByName(name);
        if (existingWithSameName != null && !existingWithSameName.getId().equals(id))
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        u.setName(name);
        u.setEmail(email);
        if (isActive != null) u.setIsActive(isActive);
        if (userTypes != null) u.setUserTypes(new HashSet<>(userTypes));
        u.setCity(city);
        u.setCountry(country);
        u.setDescription(description);
        u.setEditedIn(LocalDate.now());
        u.setPassword(originalPasswordHash);

        if (file != null && !file.isEmpty()) {
            String uniqueFileName = FileUtils.generateUniqueFileName(file);
            FileUtils.uploadImage(file, uniqueFileName);
            u.setImageProfilePath(uniqueFileName);
        }

        Users updatedUser = usersRepository.save(u);
        return ResponseEntity.ok(usersMapper.UsersToUsersProfileFullDTO(updatedUser));
    }

    // Post
    public ResponseEntity<?> signIn(UsersLogInDTO u, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(u.getName(), u.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            ResponseCookie jwtAccessCookie = jwtUtils.generateJwtCookie(userDetails);
            Users userFromDb = usersRepository.findByName(userDetails.getUsername());
            UsersProfileDTO profileDTO = usersMapper.UsersToUsersProfileDTO(userFromDb);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtAccessCookie.toString())
                    .body(profileDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<?> signUp(UsersSignUpDTO user, MultipartFile file) throws IOException {
        if (usersRepository.findByName(user.getName()) != null)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        if (file != null && !file.isEmpty()) {
            String uniqueFileName = FileUtils.generateUniqueFileName(file);
            FileUtils.uploadImage(file, uniqueFileName);
            user.setImageProfilePath(uniqueFileName);
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        Users us = usersMapper.UsersSignUpDTOtoUsers(user);

        if (us.getUserTypes() == null || us.getUserTypes().isEmpty())
            us.setUserTypes(new HashSet<>(Collections.singletonList(EUserType.MUSIC_LOVER)));

        if (us.getRoles() == null) us.setRoles(new HashSet<>());

        Role roleUser = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        us.getRoles().add(roleUser);

        usersRepository.save(us);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<?> signOut(JwtUtils jwtUtils) {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("you've been signed out!");
    }

    public ResponseEntity<Users> uploadImageProfile(MultipartFile file, Users p) throws IOException {
        String uniqueFileName = FileUtils.generateUniqueFileName(file);
        FileUtils.uploadImage(file, uniqueFileName);
        p.setImageProfilePath(uniqueFileName);
        Users users = usersRepository.save(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

    public ResponseEntity<?> sendHeartbeat(Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        usersRepository.findById(userId).ifPresent(user -> {
            user.setIsActive(true);
            user.setLastActivityTimestamp(new Date());
            usersRepository.save(user);
        });
        return ResponseEntity.ok().build();
    }

    public String getResponse(ChatRequest chatRequest) {
        return aiChatService.getResponse(chatRequest.message(), chatRequest.conversationId());
    }

    public ResponseEntity<?> signUpAsTeacher(Long id, TeacherSignUpDTO teacherDetails) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        if (user.getCity() == null || user.getCountry() == null || user.getDescription() == null)
            return ResponseEntity.badRequest().body("City, Country, Description required");

        if (user.getUserTypes().contains(EUserType.TEACHER))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User is already a teacher");

        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setPricePerLesson(teacherDetails.getPricePerLesson());
        teacher.setExperience(teacherDetails.getExperience());
        teacher.setLessonDuration(teacherDetails.getLessonDuration());
        teacher.setDateUploaded(LocalDate.now());
        teacher.setInstruments(instrumentRepository.findAllById(teacherDetails.getInstrumentsIds()));
        teacherRepository.save(teacher);

        user.getUserTypes().add(EUserType.TEACHER);
        if (user.getRoles() == null) user.setRoles(new HashSet<>());
        user.getRoles().add(roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Teacher role not found.")));
        usersRepository.save(user);

        return ResponseEntity.ok("User successfully upgraded to Teacher.");
    }

    // Delete
    public ResponseEntity<Void> deleteUser(Long userId) {
        if (!usersRepository.existsById(userId)) return ResponseEntity.notFound().build();
        usersRepository.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
