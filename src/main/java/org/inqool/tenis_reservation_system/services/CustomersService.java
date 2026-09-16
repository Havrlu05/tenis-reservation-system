package org.inqool.tenis_reservation_system.services;

import org.inqool.tenis_reservation_system.entities.Customers;
import org.inqool.tenis_reservation_system.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface CustomersService {
    Optional<Customers> readCustomer(String phone);
    Optional<Customers> getByUser(UserEntity user);
    Customers save(Customers customer);
    List<Customers> findAll();
}
