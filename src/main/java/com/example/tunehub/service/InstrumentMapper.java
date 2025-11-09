package com.example.tunehub.service;

import com.example.tunehub.dto.InstrumentDTO;
import com.example.tunehub.dto.UsersProfileDTO;
import com.example.tunehub.dto.UsersUploadProfileImageDTO;
import com.example.tunehub.model.Instrument;
import com.example.tunehub.model.Users;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstrumentMapper {

    List<InstrumentDTO> InstrumentToDTO(List<Instrument> instruments);

    Instrument InstrumentsDTOtoInstrument(InstrumentDTO i);

    InstrumentDTO InstrumentToInstrumentsDTO(Users u);

    List<Instrument> instrumentDTOListToInstrumentList(List<InstrumentDTO> list);

}
