package org.inqool.tenis_reservation_system.initializer;

import org.inqool.tenis_reservation_system.entities.Courts;
import org.inqool.tenis_reservation_system.entities.Surfaces;
import org.inqool.tenis_reservation_system.repository.CourtsRepository;
import org.inqool.tenis_reservation_system.repository.SurfacesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Value("${app.initialization.enabled:false}")
    private boolean initializationEnabled;

    @Bean
    CommandLineRunner initDatabase(SurfacesRepository surfacesRepository, CourtsRepository courtsRepository) {
        return args -> {
            if (initializationEnabled) {
                Surfaces antuka = new Surfaces();
                antuka.setName("Antuka");
                antuka.setMinuteRate(5);
                antuka = surfacesRepository.save(antuka);

                Surfaces tráva = new Surfaces();
                tráva.setName("Tráva");
                tráva.setMinuteRate(10);
                tráva = surfacesRepository.save(tráva);

                Courts court1 = new Courts();
                court1.setName("Kurt 1");
                court1.setSurface(antuka);
                courtsRepository.save(court1);

                Courts court2 = new Courts();
                court2.setName("Kurt 2");
                court2.setSurface(antuka);
                courtsRepository.save(court2);

                Courts court3 = new Courts();
                court3.setName("Kurt 3");
                court3.setSurface(tráva);
                courtsRepository.save(court3);

                Courts court4 = new Courts();
                court4.setName("Kurt 4");
                court4.setSurface(tráva);
                courtsRepository.save(court4);

            }
        };
    }
}
