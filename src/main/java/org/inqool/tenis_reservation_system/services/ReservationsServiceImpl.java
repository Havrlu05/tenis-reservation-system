package org.inqool.tenis_reservation_system.services;

import org.inqool.tenis_reservation_system.entities.Courts;
import org.inqool.tenis_reservation_system.entities.Customers;
import org.inqool.tenis_reservation_system.entities.Reservations;
import org.inqool.tenis_reservation_system.entities.UserEntity;
import org.inqool.tenis_reservation_system.repository.ReservationsRepository;
import org.inqool.tenis_reservation_system.repository.UserRepository;
import org.inqool.tenis_reservation_system.rest.dto.ReservationCreateDto;
import org.inqool.tenis_reservation_system.rest.dto.ReservationResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationsServiceImpl implements ReservationsService {
    @Autowired
    ReservationsRepository reservationsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourtsService courtsService;

    @Autowired
    CustomersService customersService;



    @Transactional
    public ReservationResponseDto create(ReservationCreateDto dto) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uživatel nenalezen"));

        Courts court = courtsService.readById(dto.getCourtId());

        boolean isOverlapping = reservationsRepository.existsOverlappingReservation(
                dto.getCourtId(), dto.getStart_time(), dto.getEnd_time()
        );
        if (isOverlapping) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "V tomto čase je kurt již obsazen jinou rezervací.");
        }


        Customers customer = customersService.readCustomer(dto.getCustomersPhone())
                .orElseGet(() -> {
                    Customers newCustomer = new Customers();
                    newCustomer.setUser(user);
                    newCustomer.setName(dto.getCustomersName());
                    newCustomer.setPhone(dto.getCustomersPhone());
                    return customersService.save(newCustomer);
                });

        long minutes = java.time.Duration.between(dto.getStart_time(), dto.getEnd_time()).toMinutes();
        double totalPrice = minutes * court.getSurface().getMinuteRate();
        if(dto.getIs_doubles()){
            totalPrice = totalPrice * 1.5;
        }

        Reservations reservation = new Reservations();
        reservation.setCourt(court);
        reservation.setCustomer(customer);
        reservation.setStartTime(dto.getStart_time());
        reservation.setEndTime(dto.getEnd_time());
        reservation.setIsDoubles(dto.getIs_doubles());

        Reservations savedReservation = reservationsRepository.save(reservation);

        ReservationResponseDto response = new ReservationResponseDto();
        response.setId(savedReservation.getId());
        response.setStartTime(savedReservation.getStartTime());
        response.setEndTime(savedReservation.getEndTime());
        response.setPrice(totalPrice);
        response.setCustomerName(customer.getName());
        response.setIsDouble(savedReservation.getIsDoubles());
        response.setCourt(court);
        return response;
    }

    @Override
    public Reservations read(Long id) {
        return reservationsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rezervace nenalezena"));
    }
    @Override
    @Transactional
    public Reservations update(Long id, ReservationCreateDto reservationCreateDto) {
        Reservations existingReservation = reservationsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rezervace nenalezena"));

        Long newCourtId = reservationCreateDto.getCourtId();
        LocalDateTime newStart = reservationCreateDto.getStart_time();
        LocalDateTime newEnd = reservationCreateDto.getEnd_time();
        
        boolean isOverlapping = reservationsRepository.existsOverlappingReservationExcludingId(
                newCourtId, newStart, newEnd, id
        );
        if (isOverlapping) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "V tomto čase je kurt již obsazen jinou rezervací.");
        }

        existingReservation.setStartTime(newStart);
        existingReservation.setEndTime(newEnd);
        existingReservation.setCourt(courtsService.readById(newCourtId));
        existingReservation.setIsDoubles(reservationCreateDto.getIs_doubles());
        existingReservation.setCustomer(customersService.readCustomer(reservationCreateDto.getCustomersPhone()).orElseGet(() -> {
            Customers newCustomer = new Customers();
            newCustomer.setUser(userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Uživatel nenalezen")));
            newCustomer.setName(reservationCreateDto.getCustomersName());
            newCustomer.setPhone(reservationCreateDto.getCustomersPhone());
            return customersService.save(newCustomer);
        }));

        return reservationsRepository.save(existingReservation);
    }

    @Override
    public void delete(Long id) {
        Reservations reservation = reservationsRepository
                .findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rezervace nenalezena"));
        reservation.setIsDeleted(true);
        reservationsRepository.save(reservation);
    }

    @Override
    public List<Reservations> findByCourtId(Long courtId){
        return reservationsRepository.findByCourtId(courtId);
    }

    public List<Reservations> getReservationsByPhone(String phone, boolean onlyFuture) {
        LocalDateTime now = LocalDateTime.now();
        List<Reservations> reservations = reservationsRepository.findByCustomerPhoneAndOptionalFuture(phone, onlyFuture, now);

        return reservations;
    }
}
