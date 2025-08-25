package org.example.anorakapi;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SightingTest {

    @Autowired
    private EntityManager entityManager; //stand-in for repository

    private Train train;
    private Station station;

    @BeforeEach
    void setUp() {
        train = new Train("Henry", "Green", "NWR3");
        entityManager.persist(train);

        station = new Station("Liverpool Street");
        entityManager.persist(station);

        entityManager.flush();
    }

    @Test
    @DisplayName("Tests creation of Sighting object and its methods")
    void testSightingCreationAndGetMethods() {
        //Using UUID as a standin for auto-generated ID
        Sighting sighting = new Sighting(station, train, "2025-08-25T17:35:42.123Z");
        entityManager.persist(sighting);
        entityManager.flush();

        assertNotNull(sighting.getId(), "ID should not be null");
        assertDoesNotThrow(() -> UUID.fromString(sighting.getId()));

        assertEquals("Liverpool Street", sighting.getStation().getName());
        assertEquals("Henry", sighting.getTrain().getName());
        assertEquals("2025-08-25T17:35:42.123Z", sighting.getTimestamp());
    }

    @Test
    @DisplayName("Sighting should throw exception if timestamp is empty.")
    void testSightingCreation_ShouldFail_IfTimestampEmptyString() {
        Exception shortNameException = assertThrows(
                IllegalArgumentException.class,
                () -> new Sighting(station, train, "")
        );
        assertEquals("Timestamp is an incorrect format.", shortNameException.getMessage());
    }

    @Test
    @DisplayName("Sighting should throw exception if timestamp is not correctly formatted.")
    void testSightingCreation_ShouldFail_IfTimestampNotCorrectlyFormatted() {
        Exception shortNameException = assertThrows(
                IllegalArgumentException.class,
                () -> new Sighting(station, train, "2025-08-2517:35:00")
        );
        assertEquals("Timestamp is an incorrect format.", shortNameException.getMessage());
    }

}
