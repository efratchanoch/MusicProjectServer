package com.example.tunehub.service;

import com.example.tunehub.dto.InstrumentResponseDTO;
import com.example.tunehub.dto.UsersProfileDTO;
import com.example.tunehub.dto.UsersUploadProfileImageDTO;
import com.example.tunehub.model.Instrument;
import com.example.tunehub.model.Users;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstrumentMapper {

    List<InstrumentResponseDTO> InstrumentToDTO(List<Instrument> instruments);

    Instrument InstrumentResponseDTOtoInstrument(InstrumentResponseDTO i);

    InstrumentResponseDTO instrumentToInstrumentResponseDTO(Users u);

    List<Instrument> instrumentResponseDTOListToInstrumentList(List<InstrumentResponseDTO> list);

}
