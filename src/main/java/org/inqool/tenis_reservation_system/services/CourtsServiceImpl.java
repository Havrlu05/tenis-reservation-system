package org.inqool.tenis_reservation_system.services;

import org.inqool.tenis_reservation_system.entities.Courts;
import org.inqool.tenis_reservation_system.repository.CourtsRepository;
import org.inqool.tenis_reservation_system.repository.ReservationsRepository;
import org.inqool.tenis_reservation_system.rest.dto.CourtCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourtsServiceImpl implements CourtsService {
    @Autowired
    private CourtsRepository courtRepository;

    @Autowired
    private SurfacesService surfacesService;

    @Autowired
    ReservationsRepository reservationsRepository;

    @Override
    public List<Courts> findAll(){
        return courtRepository.findAll();
    }

    @Override
    public Courts create(CourtCreateDto dto){
        Courts court = new Courts();
        court.setName(dto.getName());
        court.setSurface(surfacesService.findSurface(dto.getSurfaceId()));
        courtRepository.save(court);
        return court;
    }

    @Override
    public Courts readById(long id) {
        return courtRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kurt s ID " + id + " nebyl nalezen"));
    }

    @Override
    public Courts update(Long id, CourtCreateDto dto) {
        Courts existingCourt = courtRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kurt nenalezen"));

        existingCourt.setName(dto.getName());
        existingCourt.setSurface(surfacesService.findSurface(dto.getSurfaceId()));

        return courtRepository.save(existingCourt);
    }

    @Override
    public void delete(Long id) {
        Courts court = courtRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kurt nenalezen"));

        boolean hasFutureReservations = reservationsRepository.existsByCourtIdAndStartTimeAfter(id, LocalDateTime.now());
        if (hasFutureReservations) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nelze smazat kurt, který má budoucí rezervace.");
        }

        court.setIsDeleted(true);
        courtRepository.save(court);
    }
}
