package org.inqool.tenis_reservation_system.services;

import org.inqool.tenis_reservation_system.entities.Reservations;
import org.inqool.tenis_reservation_system.rest.dto.ReservationCreateDto;
import org.inqool.tenis_reservation_system.rest.dto.ReservationResponseDto;

import java.util.List;

public interface ReservationsService {
    ReservationResponseDto create(ReservationCreateDto reservationCreateDto);
    Reservations read(Long id);
    Reservations update(Long id, ReservationCreateDto reservationCreateDto);
    void delete(Long id);
    List<Reservations> findByCourtId(Long courtId);
    List<Reservations> getReservationsByPhone(String phone, boolean onlyFuture);
}
