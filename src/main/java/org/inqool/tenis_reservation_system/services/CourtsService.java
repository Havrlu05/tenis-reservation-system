package org.inqool.tenis_reservation_system.services;

import org.inqool.tenis_reservation_system.entities.Courts;
import org.inqool.tenis_reservation_system.rest.dto.CourtCreateDto;


import java.util.List;

public interface CourtsService {
    List<Courts> findAll();
    Courts create(CourtCreateDto dto);
    Courts readById(long id);
    Courts update(Long id, CourtCreateDto dto);
    void delete(Long id);
}
