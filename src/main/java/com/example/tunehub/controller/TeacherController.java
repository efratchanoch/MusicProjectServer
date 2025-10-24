package com.example.tunehub.controller;

import com.example.tunehub.service.TeacherMapper;
import com.example.tunehub.service.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
