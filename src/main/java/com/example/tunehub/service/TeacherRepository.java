package com.example.tunehub.service;

import com.example.tunehub.model.Post;
import com.example.tunehub.model.SheetMusic;
import com.example.tunehub.model.Teacher;
import com.example.tunehub.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findTeacherById(Long id);

    List<Teacher> findAllByName(String name);

    List<Teacher> findAllByCity(String city);

    List<Teacher> findAllByCountry(String country);

    List<Teacher> findAllByLessonDuration(Double lessonDuration);

    List<Teacher> findAllByExperience(int experience);

    List<Teacher> findAllByInstrumentsId(Long instrument_id);

    List<Teacher> findAllByNameContainingIgnoreCase(String name);

    List<Teacher> findAllTop5ByNameContainingIgnoreCase(String name);
}
