package org.inqool.tenis_reservation_system.rest.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationCreateDto {
    private Long courtId;
    private String customersPhone;
    private String customersName;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private Boolean is_doubles;
}
