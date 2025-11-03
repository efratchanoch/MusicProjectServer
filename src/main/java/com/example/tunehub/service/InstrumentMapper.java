package com.example.tunehub.service;

import com.example.tunehub.dto.InstrumentsDTO;
import com.example.tunehub.dto.UsersProfileDTO;
import com.example.tunehub.dto.UsersUploadProfileImageDTO;
import com.example.tunehub.model.Instrument;
import com.example.tunehub.model.Users;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstrumentMapper {

    List<InstrumentsDTO> InstrumentToDTO(List<Instrument> instruments);

    Instrument InstrumentsDTOtoInstrument(InstrumentsDTO i);

    InstrumentsDTO InstrumentToInstrumentsDTO(Users u);

}
