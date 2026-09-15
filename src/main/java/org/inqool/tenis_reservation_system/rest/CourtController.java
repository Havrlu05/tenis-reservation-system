package org.inqool.tenis_reservation_system.rest;

import org.inqool.tenis_reservation_system.entities.Courts;
import org.inqool.tenis_reservation_system.rest.dto.CourtCreateDto;
import org.inqool.tenis_reservation_system.services.CourtsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path ="/api/court/")
public class CourtController {

    @Autowired
    private CourtsService courtsService;


    @GetMapping("readAll")
    public ResponseEntity<List<Courts>> getAllCourts() {
        List<Courts> courts = courtsService.findAll();
        return ResponseEntity.ok(courts);
    }

    @GetMapping("read/{id}")
    public ResponseEntity<Courts> getCourtById(@PathVariable Long id) {
        Courts court = courtsService.readById(id);
        if (court == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kurt s ID " + id + " nebyl nalezen.");
        }
        return ResponseEntity.ok(court);
    }

    @PostMapping("createOne")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Courts> createCourt(@RequestBody CourtCreateDto dto) {
        try {
            Courts savedCourt = courtsService.create(dto);
            return ResponseEntity.ok(savedCourt);
        } catch (IllegalArgumentException e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourt(@PathVariable Long id) {
        try {
            courtsService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kurt s ID " + id + " nebyl nalezen pro smazání.");
        }
    }

    @PutMapping("update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Courts> updateCourt(@PathVariable Long id, @RequestBody CourtCreateDto dto) {
        try {
            Courts updatedCourt = courtsService.update(id, dto);
            return ResponseEntity.ok(updatedCourt);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
