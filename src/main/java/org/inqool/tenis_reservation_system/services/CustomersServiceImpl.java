package org.inqool.tenis_reservation_system.services;

import org.inqool.tenis_reservation_system.entities.Customers;
import org.inqool.tenis_reservation_system.entities.UserEntity;
import org.inqool.tenis_reservation_system.repository.CustomersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CustomersServiceImpl implements CustomersService {

    @Autowired
    private CustomersRepository customersRepository;

    @Override
    public Customers readCustomer(String phone){
        return customersRepository.findByPhone(phone).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Zákazník nenalezen"));
    }

    @Override
    public Optional<Customers> getByUser(UserEntity user){
        return customersRepository.findByUser(user);
    }

    @Override
    public Customers save(Customers customers){
        return customersRepository.save(customers);
    }
}
