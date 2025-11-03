package com.example.tunehub.service;

import com.example.tunehub.model.EUserType;
import com.example.tunehub.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersRepository  extends JpaRepository<Users, Long> {

    Users findUsersByName(String name);

    Users save(Users user);//

    Users findUsersById(Long id);

    List<Users> findUsersByEUserType(EUserType user_type);//

    List<Users> findByTeacherId(Long teacherId);

    // List<Users> findByLikes(List<Users> users);

    // List<Users> findTop10ByLikesOrderByLikesDesc();

    boolean existsById(Long id);

    void deleteById(Long id);


    Users findByName(String name);
}
