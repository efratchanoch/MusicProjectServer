package com.example.tunehub.service;

import com.example.tunehub.dto.*;
import com.example.tunehub.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;


@Mapper(componentModel = "spring")
public  interface  UsersMapper {


    @Mapping(
            target = "imageProfilePath",
            expression = "java(com.example.tunehub.service.FileUtils.imageToBase64(u.getImageProfilePath()))")
    UsersProfileDTO UsersToUsersProfileDTO(Users u);

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "isActive", expression = "java(true)")
    Users UsersSignUpDTOtoUsers(UsersSignUpDTO u);

    Users UsersLogInDTOtoUsers(UsersLogInDTO u);

    @Mapping(target = "profile", source = "u")
    UsersMusiciansDTO UsersToUsersMusiciansDTO(Users u);

    List<UsersUploadProfileImageDTO> UsersToDTO(List<Users> users);

    Users UsersProfileImageDTOtoUsers(UsersUploadProfileImageDTO u);

    List<UsersMusiciansDTO> UsersToUsersMusiciansDTO(List<Users> u);

//
//    @Mapping(
//            target = ".",
//            expression = "java(usersRepository.findById(u.getId()).orElse(null))"
//    )
    Users usersDTOtoUsers(UsersDTO u);

    default UsersUploadProfileImageDTO usersToDTO(Users u) throws IOException {
        UsersUploadProfileImageDTO usersUploadProfileImageDTO = new UsersUploadProfileImageDTO();
        usersUploadProfileImageDTO.setId(u.getId());
        usersUploadProfileImageDTO.setImagePath(u.getImageProfilePath());
        return usersUploadProfileImageDTO;
    }

//    default UsersProfileDTO usersToDTO(Users u) throws IOException {
//        UsersProfileDTO usersProfileDTO = new UsersProfileDTO();
//        usersProfileDTO.setId(u.getId());
//        usersProfileDTO.setImageProfilePath(u.getImageProfilePath());
//        return usersProfileDTO;
//    }


}
