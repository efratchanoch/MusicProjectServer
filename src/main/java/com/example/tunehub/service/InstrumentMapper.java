package com.example.tunehub.service;

import com.example.tunehub.dto.InstrumentResponseDTO;
import com.example.tunehub.dto.UsersProfileDTO;
import com.example.tunehub.dto.UsersUploadProfileImageDTO;
import com.example.tunehub.model.Instrument;
import com.example.tunehub.model.Users;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstrumentMapper {

    Instrument InstrumentResponseDTOtoInstrument(InstrumentResponseDTO i);

    InstrumentResponseDTO instrumentToInstrumentResponseDTO(Users u);

    List<Instrument> instrumentResponseDTOListToInstrumentList(List<InstrumentResponseDTO> list);
    List<InstrumentResponseDTO> instrumentListToInstrumentResponseDTOList(List<Instrument> list);

    @Named("mapInstrumentListWithoutCreate")
    @IterableMapping(qualifiedByName = "mapInstrument")
    List<Instrument> mapInstrumentListWithoutCreate(List<InstrumentResponseDTO> list);

    @Named("mapInstrument")
    Instrument mapInstrument(InstrumentResponseDTO i);
}
