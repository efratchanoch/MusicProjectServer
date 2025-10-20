package com.example.tunehub.service;

import com.example.tunehub.model.UserType;
import com.example.tunehub.model.Users;

import java.util.List;

public interface UsersRepository {
    Users findUsersByUserName(String name);

    List<Users> findUsers();

    Users save(Users user);//

    Users findUsersById(Long id);

    List<Users> findUsersByUserType(UserType user_type);//

    List<Users> findByTeacherId(Long teacherId);

    // List<Users> findByLikes(List<Users> users);

    List<Users> findTop10ByLikesOrderByLikesDesc();

    boolean existsById(Long id);

    void deleteById(Long id);
}
