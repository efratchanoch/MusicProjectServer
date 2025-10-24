package com.example.tunehub.service;

import com.example.tunehub.dto.UsersProfileImageDTO;
import com.example.tunehub.model.Users;
import org.mapstruct.Mapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    default UsersProfileImageDTO usersToDTO(Users u) throws IOException {
        UsersProfileImageDTO usersProfileImageDTO = new UsersProfileImageDTO();
        usersProfileImageDTO.setId(u.getId());
        usersProfileImageDTO.setImagePath(u.getImageProfilePath());


        Path fileName = Paths.get(u.getImageProfilePath()); //לוקח את הנתיב של התמונה
        byte[] byteImage = Files.readAllBytes(fileName); //מעביר למערך של ביטים
        //כדי להפחית את תעבורת הרשת, נקודד למחרוזת של בייס64 שהיא קטנה יותר
        usersProfileImageDTO.setImage(Base64.getEncoder().encodeToString(byteImage));


        return usersProfileImageDTO;
    }

}
