package org.example.anorakapi;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class StationTest {

    @Autowired
    private EntityManager entityManager; //stand-in for repository

    @Test
    @DisplayName("Tests creation of Station object and its methods")
    void testStationCreationAndGetMethods() {
        Station station = new Station("Liverpool Street");

        entityManager.persist(station);
        entityManager.flush();
        assertNotNull(station.getId(), "ID should not be null");
        assertDoesNotThrow(() -> UUID.fromString(station.getId()));

        assertEquals("Liverpool Street", station.getName());
    }

    @Test
    @DisplayName("Station should throw exception if name is empty string.")
    void testStationCreation_ShouldFail_IfNameEmptyString() {
        Exception shortNameException = assertThrows(
                IllegalArgumentException.class,
                () -> new Station("")
        );
        assertEquals("Station name cannot be empty", shortNameException.getMessage());
    }

}
