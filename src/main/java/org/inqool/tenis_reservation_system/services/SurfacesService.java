package org.inqool.tenis_reservation_system.services;

import org.inqool.tenis_reservation_system.entities.Surfaces;

import java.util.Optional;


public interface SurfacesService {
    Surfaces findSurface(Long surfaceId);
}
