package org.inqool.tenis_reservation_system.rest;

import org.inqool.tenis_reservation_system.entities.Customers;
import org.inqool.tenis_reservation_system.repository.CustomersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path ="/api/customers/")
public class CustomersController {
    @Autowired
    CustomersRepository customersRepository;


    @GetMapping("findAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Customers>> findAll(){
        return ResponseEntity.ok(customersRepository.findAll());
    }
}
