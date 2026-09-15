package org.inqool.tenis_reservation_system.rest;

import org.inqool.tenis_reservation_system.entities.Reservations;
import org.inqool.tenis_reservation_system.rest.dto.ReservationCreateDto;
import org.inqool.tenis_reservation_system.rest.dto.ReservationResponseDto;
import org.inqool.tenis_reservation_system.services.ReservationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping(path ="/api/reservation/")
public class ReservationController {
    @Autowired
    private ReservationsService reservationsService;

    @PostMapping("create")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ReservationResponseDto> create(@RequestBody ReservationCreateDto reservation) {
        return ResponseEntity.ok(reservationsService.create(reservation));
    }

    @GetMapping("read/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Reservations> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationsService.read(id));
    }

    @GetMapping("court/{courtId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Reservations>> getReservationsByCourt(@PathVariable Long courtId) {
        List<Reservations> reservations = reservationsService.findByCourtId(courtId);
        return ResponseEntity.ok(reservations);
    }

    @PutMapping("update/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Reservations> updateReservation(@PathVariable Long id, @RequestBody ReservationCreateDto dto) {
        try {
            Reservations updated = reservationsService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        try {
            reservationsService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rezervace s ID: " + id + " nebyla nalezena pro smazání.");
        }
    }

    @GetMapping("phone/{phone}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Reservations>> getReservationsByPhone(@PathVariable String phone, @RequestParam(required = false, defaultValue = "false") boolean onlyFuture) {

        List<Reservations> reservations = reservationsService.getReservationsByPhone(phone, onlyFuture);
        return ResponseEntity.ok(reservations);
    }
}
