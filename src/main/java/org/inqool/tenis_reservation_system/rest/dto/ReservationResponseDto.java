package org.inqool.tenis_reservation_system.rest.dto;

import lombok.Getter;
import lombok.Setter;
import org.inqool.tenis_reservation_system.entities.Courts;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationResponseDto {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Courts court;
    private Double price;
    private String customerName;
    private Boolean isDouble;

}