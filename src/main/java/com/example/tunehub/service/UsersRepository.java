package com.example.tunehub.service;

import com.example.tunehub.dto.UsersMusiciansDTO;
import com.example.tunehub.model.EUserType;
import com.example.tunehub.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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




//    UsersMusiciansDTO findMusicianDtoById(Long id);

    List<Users> findAllByLastActivityTimestampBeforeAndIsActiveIsTrue(Date threshold);

    //Optional<Users> findByUsername(String username);

    Users findByName(String name);

    List<Users> findByNameContainingIgnoreCase(String namePart);


    @Modifying
    @Query("UPDATE Users u SET u.followerCount = :count WHERE u.id = :id")
    @Transactional
    void updateFollowerCount(@Param("id") Long userId, @Param("count") int count);

}

