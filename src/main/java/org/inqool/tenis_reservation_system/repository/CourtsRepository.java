package org.inqool.tenis_reservation_system.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.inqool.tenis_reservation_system.entities.Courts;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class CourtsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Courts save(Courts court) {
        if (court.getId() == null) {
            entityManager.persist(court);
            return court;
        } else {
            return entityManager.merge(court);
        }
    }

    public Optional<Courts> findById(Long id) {
        Courts court = entityManager.find(Courts.class, id);
        return Optional.ofNullable(court);
    }

    public List<Courts> findAll() {
        return entityManager.createQuery("SELECT c FROM Courts c", Courts.class).getResultList();
    }
}