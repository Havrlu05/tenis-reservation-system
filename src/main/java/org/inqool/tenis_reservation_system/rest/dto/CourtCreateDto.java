package org.inqool.tenis_reservation_system.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourtCreateDto {
    private String name;
    private Long  surfaceId;
}
