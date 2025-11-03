package com.example.tunehub.service;

import com.example.tunehub.dto.InstrumentDTO;
import com.example.tunehub.model.Instrument;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstrumentMapper {
    List<InstrumentDTO> InstrumentToDTO(List<Instrument> instruments);

}
