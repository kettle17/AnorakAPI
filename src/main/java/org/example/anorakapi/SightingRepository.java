package org.example.anorakapi;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SightingRepository extends JpaRepository<Sighting, String> {
    List<Sighting> findAllByTrain(Train train);
}
