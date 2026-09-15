package org.inqool.tenis_reservation_system.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customers {
    @Id
    private String phone;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String name;
}
