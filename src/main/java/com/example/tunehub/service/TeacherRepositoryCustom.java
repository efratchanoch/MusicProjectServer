package com.example.tunehub.service;

import com.example.tunehub.model.Teacher;

import java.util.List;

public interface TeacherRepositoryCustom {

    // 1. חתימה לפונקציית הסינון המלאה
    List<Teacher> findTeachersByFilters(
            String city, String country, String priceRange, Integer duration,
            String experience, Long instrumentId, String search);

    // 2. חתימה לפונקציה לשליפת ערים ייחודיות (Distinct)
    List<String> getAllDistinctCities();
}
