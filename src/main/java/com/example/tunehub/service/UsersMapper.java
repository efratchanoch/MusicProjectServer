package com.example.tunehub.service;

import com.example.tunehub.dto.*;
import com.example.tunehub.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;


@Mapper(componentModel = "spring" , uses = {RoleMapper.class})
public  interface  UsersMapper {


    @Mapping(
            target = "imageProfilePath",
            expression = "java(com.example.tunehub.service.FileUtils.imageToBase64(u.getImageProfilePath()))")
    UsersProfileDTO UsersToUsersProfileDTO(Users u);

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "isActive", expression = "java(true)")
    @Mapping(target = "name", source = "name")
    Users UsersSignUpDTOtoUsers(UsersSignUpDTO u);

    @Mapping(
            target = "imageProfilePath",
            expression = "java(com.example.tunehub.service.FileUtils.imageToBase64(u.getImageProfilePath()))")
    @Mapping(target = "teacher", source = "u.teacherDetails")
    UsersProfileFullDTO UsersToUsersProfileFullDTO(Users u);

    Users UsersLogInDTOtoUsers(UsersLogInDTO u);

    @Mapping(
            target = "profile", source = "u"
    )
    @Mapping(target = "city", source = "u.city")
    @Mapping(target = "country", source = "u.country")
    @Mapping(target = "isActive", source = "u.active")
    @Mapping(target = "description", source = "u.description")
    @Mapping(target = "userTypes", source = "u.userTypes")
    @Mapping(target = "createdAt", source = "u.createdAt")
    @Mapping(target = "teacher", source = "u.teacherDetails")
    public abstract UsersMusiciansDTO UsersToUsersMusiciansDTO(Users u);
    public abstract List<UsersMusiciansDTO> UsersToUsersMusiciansDTO(List<Users> u);

    List<UsersUploadProfileImageDTO> UsersToDTO(List<Users> users);

    Users UsersProfileImageDTOtoUsers(UsersUploadProfileImageDTO u);

    //List<UsersMusiciansDTO> UsersToUsersMusiciansDTO(List<Users> u);

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

    @Mapping(target = "profile", source = "u")
    UsersSearchDTO usersToUsersSearchDTO(Users u);

    List<UsersSearchDTO> usersListToUsersSearchDTOList(List<Users> u);

//    default UsersProfileDTO usersToDTO(Users u) throws IOException {
//        UsersProfileDTO usersProfileDTO = new UsersProfileDTO();
//        usersProfileDTO.setId(u.getId());
//        usersProfileDTO.setImageProfilePath(u.getImageProfilePath());
//        return usersProfileDTO;
//    }


}
