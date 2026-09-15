package org.inqool.tenis_reservation_system.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "surfaces")
@Getter
@Setter
public class Surfaces {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer minuteRate;
}
