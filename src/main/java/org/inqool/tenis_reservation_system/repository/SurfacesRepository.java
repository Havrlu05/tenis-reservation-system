package org.inqool.tenis_reservation_system.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.inqool.tenis_reservation_system.entities.Surfaces;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public class SurfacesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Surfaces save(Surfaces surface) {
        if (surface.getId() == null) {
            entityManager.persist(surface);
            return surface;
        } else {
            return entityManager.merge(surface);
        }
    }

    public Surfaces findFirstById(Long id) {
        return entityManager.find(Surfaces.class, id);
    }
}