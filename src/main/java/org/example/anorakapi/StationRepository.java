package org.example.anorakapi;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, String> {
    Optional<Station> findByName(String name);
}
