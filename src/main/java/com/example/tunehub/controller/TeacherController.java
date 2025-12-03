package com.example.tunehub.controller;

import com.example.tunehub.dto.TeacherListingDTO;
import com.example.tunehub.dto.UsersMusiciansDTO;
import com.example.tunehub.model.EUserType;
import com.example.tunehub.model.Role;
import com.example.tunehub.model.Teacher;
import com.example.tunehub.model.Users;
import com.example.tunehub.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.tunehub.dto.TeacherListingDTO; // ⬅️ ייבוא DTO
import com.example.tunehub.service.TeacherMapper; // ⬅️ ייבוא המאפר

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
    public ResponseEntity<List<Users>> getTeachersByName(@PathVariable String name) {
        try {
            List<Users> users = usersRepository.findAllByNameContainingIgnoreCase(name);

            if (users == null || users.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            List<Users> teachers = users.stream()
                    .filter(u -> u.getUserTypes() != null &&
                            u.getUserTypes().contains(EUserType.TEACHER))
                    .toList();

            if (teachers.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(teachers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
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



}



