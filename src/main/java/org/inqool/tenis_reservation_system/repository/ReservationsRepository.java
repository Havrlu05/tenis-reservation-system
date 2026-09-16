package org.inqool.tenis_reservation_system.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.inqool.tenis_reservation_system.entities.Reservations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ReservationsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Reservations save(Reservations reservation) {
        if (reservation.getId() == null) {
            entityManager.persist(reservation);
            return reservation;
        } else {
            return entityManager.merge(reservation);
        }
    }

    public Optional<Reservations> findById(Long id) {
        Reservations reservation = entityManager.find(Reservations.class, id);
        return Optional.ofNullable(reservation);
    }

    public boolean existsByCourtIdAndStartTimeAfter(Long courtId, LocalDateTime startTime) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(r) FROM Reservations r WHERE r.court.id = :courtId AND r.startTime > :startTime AND r.isDeleted = false", Long.class)
                .setParameter("courtId", courtId)
                .setParameter("startTime", startTime)
                .getSingleResult();

        return count > 0;
    }

    public boolean existsOverlappingReservation(Long courtId, LocalDateTime startTime, LocalDateTime endTime) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(r) FROM Reservations r WHERE r.court.id = :courtId " +
                                "AND r.startTime < :endTime AND r.endTime > :startTime", Long.class)
                .setParameter("courtId", courtId)
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime)
                .getSingleResult();

        return count > 0;
    }

    public boolean existsOverlappingReservationExcludingId(Long courtId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId) {
        String jpql = "SELECT COUNT(r) FROM Reservations r " +
                "WHERE r.court.id = :courtId " +
                "AND r.isDeleted = false " +
                "AND r.id != :excludeId " +
                "AND r.startTime < :endTime " +
                "AND r.endTime > :startTime";

        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("courtId", courtId)
                .setParameter("excludeId", excludeId)
                .setParameter("endTime", endTime)
                .setParameter("startTime", startTime)
                .getSingleResult();

        return count > 0;
    }

    public List<Reservations> findByCourtId(Long courtId) {
        return entityManager.createQuery(
                        "SELECT r FROM Reservations r WHERE r.court.id = :courtId ORDER BY r.startTime DESC", Reservations.class)
                .setParameter("courtId", courtId)
                .getResultList();
    }

    public List<Reservations> findByCustomerPhoneAndOptionalFuture(String phone, boolean onlyFuture, LocalDateTime now) {
        String jpql = "SELECT r FROM Reservations r WHERE r.customer.phone = :phone " +
                "AND (:onlyFuture = false OR r.startTime > :now) " +
                "ORDER BY r.startTime ASC";

        return entityManager.createQuery(jpql, Reservations.class)
                .setParameter("phone", phone)
                .setParameter("onlyFuture", onlyFuture)
                .setParameter("now", now)
                .getResultList();
    }
}

