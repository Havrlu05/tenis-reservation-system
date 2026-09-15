package org.inqool.tenis_reservation_system.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "courts")
@Getter
@Setter
public class Courts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "surface_id", nullable = false)
    private Surfaces surface;

    private Boolean isDeleted = false;
}