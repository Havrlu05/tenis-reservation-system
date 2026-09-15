package org.inqool.tenis_reservation_system.services;

import org.inqool.tenis_reservation_system.entities.Surfaces;
import org.inqool.tenis_reservation_system.repository.SurfacesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SurfacesServiceImpl implements SurfacesService {
    @Autowired
    SurfacesRepository surfacesRepository;

    @Override
    public Surfaces findSurface(Long surfaceId){
        return surfacesRepository.findFirstById(surfaceId);
    }
}
