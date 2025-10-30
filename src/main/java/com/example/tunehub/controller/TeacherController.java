package com.example.tunehub.controller;

import com.example.tunehub.model.Post;
import com.example.tunehub.model.Teacher;
import com.example.tunehub.service.TeacherMapper;
import com.example.tunehub.service.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@CrossOrigin
public class TeacherController {
    private TeacherRepository teacherRepository;
    private TeacherMapper teacherMapper;

    @Autowired
    public TeacherController(TeacherRepository teacherRepository, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
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
}
