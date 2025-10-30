package com.example.tunehub.service;

import com.example.tunehub.dto.UsersUploadProfileImageDTO;
import com.example.tunehub.model.Users;
import org.mapstruct.Mapper;

import java.io.IOException;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    List<UsersUploadProfileImageDTO> UsersToDTO(List<Users> users);
    Users UsersProfileImageDTOToUsers(UsersUploadProfileImageDTO u);
    default UsersUploadProfileImageDTO usersToDTO(Users u) throws IOException {
        UsersUploadProfileImageDTO usersUploadProfileImageDTO =new UsersUploadProfileImageDTO();
        usersUploadProfileImageDTO.setId(u.getId());
        usersUploadProfileImageDTO.setImagePath(u.getImageProfilePath());


//        Path fileName = Paths.get(u.getImageProfilePath()); //לוקח את הנתיב של התמונה
//        byte[] byteImage = Files.readAllBytes(fileName); //מעביר למערך של ביטים
//        //כדי להפחית את תעבורת הרשת, נקודד למחרוזת של בייס64 שהיא קטנה יותר
//        usersProfileImageDTO.setImage(Base64.getEncoder().encodeToString(byteImage));


        return usersUploadProfileImageDTO;
    }

}
