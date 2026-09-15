package org.inqool.tenis_reservation_system.rest.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationResponseDto {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double price;
    private String customerName;
    private Boolean isDouble;

}