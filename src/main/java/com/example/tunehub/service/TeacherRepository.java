package com.example.tunehub.service;


import com.example.tunehub.model.Teacher;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findTeacherById(Long id);

    List<Teacher> findAllByLessonDuration(Double lessonDuration);

    List<Teacher> findAllByExperience(int experience);

    List<Teacher> findAllByInstrumentsId(Long instrument_id);

   // List<Teacher> findAllByNameContainingIgnoreCase(String name);

   //List<Teacher> findAllTop5ByNameContainingIgnoreCase(String name);

   // List<Teacher> findAllByCountry(String country);

   // List<Teacher> findAllByCity(String city);

//    List<Teacher> findTeachersByFilters(String city, String country, String priceRange, Integer duration, String experience, Long instrumentId, String search);
}
