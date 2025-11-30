package com.example.tunehub.service;

import com.example.tunehub.model.Instrument;
import com.example.tunehub.model.Teacher;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
// import java.util.function.Predicate; // ❌ הוסר
import java.util.stream.Collectors;

@Repository // ⬅️ הוסף את זה מעל הקלאס
public class TeacherRepositoryCustomImpl implements TeacherRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Teacher> findTeachersByFilters(
            String city, String country, String priceRange, Integer duration,
            String experience, Long instrumentId, String search) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Teacher> cq = cb.createQuery(Teacher.class);
        Root<Teacher> teacher = cq.from(Teacher.class);

        // עכשיו List<Predicate> משתמש בטיפוס הנכון
        List<Predicate> predicates = new ArrayList<>();

        // ----------------------- 1 & 2: City / Country ------------------------
        if (city != null && !city.isEmpty()) {
            // ❌ ה-Casting הוסר
            predicates.add(cb.equal(teacher.get("city"), city));
        }
        if (country != null && !country.isEmpty()) {
            predicates.add(cb.equal(teacher.get("country"), country));
        }

        // ----------------------- 3: Price Range (מחיר) ------------------------
        if (priceRange != null && !priceRange.isEmpty()) {
            Predicate pricePredicate = null;
            switch (priceRange) {
                case "0-100":
                    pricePredicate = cb.lessThanOrEqualTo(teacher.get("pricePerLesson"), 100.0);
                    break;
                case "100-200":
                    pricePredicate = cb.between(teacher.get("pricePerLesson"), 100.0, 200.0);
                    break;
                case "200-plus":
                    pricePredicate = cb.greaterThan(teacher.get("pricePerLesson"), 200.0);
                    break;
            }
            if (pricePredicate != null) { predicates.add(pricePredicate); }
        }

        // ----------------------- 4: Lesson Duration (משך שיעור) ------------------------
        if (duration != null) {
            predicates.add(cb.equal(teacher.get("lessonDuration"), duration));
        }

        // ----------------------- 5: Experience (ניסיון) ------------------------
        if (experience != null && !experience.isEmpty()) {
            Predicate experiencePredicate = null;
            switch (experience) {
                case "0-2":
                    experiencePredicate = cb.lessThanOrEqualTo(teacher.get("experience"), 2);
                    break;
                case "2-5":
                    experiencePredicate = cb.between(teacher.get("experience"), 2, 5);
                    break;
                case "5-plus":
                    experiencePredicate = cb.greaterThan(teacher.get("experience"), 5);
                    break;
            }
            if (experiencePredicate != null) { predicates.add(experiencePredicate); }
        }

        // ----------------------- 6: Instrument ID (כלי נגינה) ------------------------
        if (instrumentId != null) {
            // יצירת Join לטבלת כלי הנגינה
            Join<Teacher, Instrument> instrumentJoin = teacher.join("instruments");
            // סינון לפי ה-ID של הכלי
            predicates.add(cb.equal(instrumentJoin.get("id"), instrumentId));
        }

        // ----------------------- 7: Search Query (חיפוש כללי) ------------------------
        if (search != null && !search.isEmpty()) {
            String searchPattern = "%" + search.toLowerCase() + "%";
            Predicate searchPredicate = cb.or(
                    cb.like(cb.lower(teacher.get("name")), searchPattern),
                    cb.like(cb.lower(teacher.get("description")), searchPattern)
            );
            predicates.add(searchPredicate);
        }

        // ----------------------- שילוב כל התנאים -----------------------
        // השורה הזו עובדת כעת מכיוון שהטיפוסים תואמים:
        cq.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<String> getAllDistinctCities() {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        // 1. הגדרת המקור כ-Teacher, מה שמבטיח ש-Hibernate יבצע JOIN או סינון למורים בלבד
        Root<Teacher> teacher = cq.from(Teacher.class);

        // 2. הגדרת ה-SELECT: בחר את שדה 'city' (שנמצא בטבלת Users אך נגיש דרך Teacher)
        cq.select(teacher.get("city")).distinct(true);

        // 3. הגדרת תנאים (WHERE)
        List<Predicate> predicates = new ArrayList<>();

        // ודא שהעיר לא ריקה או null
        predicates.add(cb.isNotNull(teacher.get("city")));
        predicates.add(cb.notEqual(teacher.get("city"), ""));

        cq.where(predicates.toArray(new Predicate[0]));

        // 4. שליפה
        List<String> cities = entityManager.createQuery(cq)
                .getResultList();

        // 5. מיון
        return cities.stream().sorted().collect(Collectors.toList());
    }
}

