package com.example.tunehub.service;

import com.example.tunehub.dto.TeacherListingDTO;
import com.example.tunehub.model.Post;
import com.example.tunehub.model.SheetMusic;
import com.example.tunehub.model.Teacher;
import com.example.tunehub.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long>, TeacherRepositoryCustom {
    Teacher findTeacherById(Long id);

    List<Teacher> findAllByUser_Name(String name);

    List<Teacher> findAllByUser_City(String city);

    List<Teacher> findAllByUser_Country(String country);

    List<Teacher> findAllByLessonDuration(Double lessonDuration);

    List<Teacher> findAllByExperience(int experience);

    List<Teacher> findAllByInstrumentsId(Long instrument_id);

    List<Teacher> findAllByNameContainingIgnoreCase(String name);

    List<Teacher> findAllTop5ByNameContainingIgnoreCase(String name);
}
