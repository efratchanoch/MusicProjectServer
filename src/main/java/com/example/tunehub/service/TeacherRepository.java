package com.example.tunehub.service;

import com.example.tunehub.model.Post;
import com.example.tunehub.model.Teacher;
import com.example.tunehub.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findTeacherById(Long id);
}
