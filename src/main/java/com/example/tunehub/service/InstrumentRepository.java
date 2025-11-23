package com.example.tunehub.service;

import com.example.tunehub.model.Instrument;
import com.example.tunehub.model.Users;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    List<Instrument> findAllByUsers_Id(Long usersId);

    List<Instrument> findAllByTeachers_Id(Long teachersId);

    List<Instrument> findAllBySheetsMusic_Id(Long sheetMusicId);

    Optional<Instrument> findByNameIgnoreCase(String name);

    Instrument findByName(String name);
}
