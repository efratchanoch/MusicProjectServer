package com.example.tunehub.service;

import com.example.tunehub.dto.*;
import com.example.tunehub.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;
import java.util.List;


@Mapper(componentModel = "spring")
public interface UsersMapper {
    List<UsersUploadProfileImageDTO> UsersToDTO(List<Users> users);

    Users UsersProfileImageDTOtoUsers(UsersUploadProfileImageDTO u);

    UsersProfileDTO UsersToUsersProfileDTO(Users u);

    Users UsersSignUpDTOtoUsers(UsersSignUpDTO u);

    Users UsersLogInDTOtoUsers(UsersLogInDTO u);

    @Mapping(source = "u", target = "profile")
    UsersMusiciansDTO UsersToUsersMusiciansDTO(Users u);

    List<UsersMusiciansDTO> UsersToUsersMusiciansDTO(List<Users> u);

    default UsersUploadProfileImageDTO usersToDTO(Users u) throws IOException {
        UsersUploadProfileImageDTO usersUploadProfileImageDTO = new UsersUploadProfileImageDTO();
        usersUploadProfileImageDTO.setId(u.getId());
        usersUploadProfileImageDTO.setImagePath(u.getImageProfilePath());
        return usersUploadProfileImageDTO;
    }


}
