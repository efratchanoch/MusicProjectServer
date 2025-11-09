package com.example.tunehub.controller;

import com.example.tunehub.model.Teacher;
import com.example.tunehub.service.TeacherMapper;
import com.example.tunehub.service.TeacherRepository;
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

}
