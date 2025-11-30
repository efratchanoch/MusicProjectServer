package com.example.tunehub.service;

import com.example.tunehub.dto.*;
import com.example.tunehub.model.Teacher;
import com.example.tunehub.model.Users;
import org.mapstruct.Mapper;

import java.io.IOException;
import java.util.List;

@Mapper(componentModel = "spring", uses = UsersMapper.class)
public interface TeacherMapper {

    List<UsersSearchDTO> teacherListToUsersSearchDTOList(List<Teacher> t);
}
