package org.inqool.tenis_reservation_system.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.inqool.tenis_reservation_system.entities.Customers;
import org.inqool.tenis_reservation_system.entities.UserEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class CustomersRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Customers save(Customers customer) {
        return entityManager.merge(customer);
    }

    public Optional<Customers> findByPhone(String phone) {
        Customers customer = entityManager.find(Customers.class, phone);
        return Optional.ofNullable(customer);
    }

    public Optional<Customers> findByUser(UserEntity user) {
        return entityManager.createQuery(
                        "SELECT c FROM Customers c WHERE c.user = :user", Customers.class)
                .setParameter("user", user)
                .getResultStream()
                .findFirst();
    }

    public List<Customers> findAll() {
        return entityManager.createQuery("SELECT c FROM Customers c", Customers.class).getResultList();
    }



}