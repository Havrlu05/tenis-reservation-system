package org.inqool.tenis_reservation_system.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
public class Reservations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "court_id", nullable = false)
    private Courts court;

    @ManyToOne
    @JoinColumn(name = "customer_phone", referencedColumnName = "phone", nullable = false)
    private Customers customer;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean isDoubles = false;

    private Boolean isDeleted = false;
}
