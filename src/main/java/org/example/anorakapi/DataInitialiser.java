package org.example.anorakapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class DataInitialiser {

    @Bean
    CommandLineRunner initData(
            TrainRepository trainRepo,
            StationRepository stationRepo,
            SightingRepository sightingRepo
    ) {
        return args -> {
            // Prepopulate Trains
            Train thomas = trainRepo.save(new Train("Thomas", "Blue", "T1192A"));
            Train henry  = trainRepo.save(new Train("Henry", "Green", "H234B"));

            Station liverpool  = stationRepo.save(new Station("Liverpool Street"));
            Station kingsCross = stationRepo.save(new Station("King's Cross"));

            Sighting sighting1 = new Sighting(liverpool, thomas, "2025-08-25T10:15:30Z");
            Sighting sighting2 = new Sighting(kingsCross, henry, "2025-08-25T11:45:00Z");
            sightingRepo.saveAll(Arrays.asList(sighting1, sighting2));
        };
    }
}