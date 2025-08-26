package org.example.anorakapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StationTest {

    @Autowired
    private StationRepository stationRepository;

    @Test
    @DisplayName("Tests creation of Station object, repository and its methods")
    void testStationCreationAndGetMethods() {
        Station station = new Station("Liverpool Street");
        Station savedStation = stationRepository.save(station).block();

        assertNotNull(savedStation.getId(), "ID should not be null");
        assertEquals("Liverpool Street", savedStation.getName());
    }

    @Test
    @DisplayName("Station should throw exception if name is empty string.")
    void testStationCreation_ShouldFail_IfNameEmptyString() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Station("")
        );
        assertEquals("Station name cannot be empty", ex.getMessage());
    }

    @Test
    @DisplayName("Station should throw exception if station name null.")
    void testStationCreation_ShouldFail_IfStationNameNull() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Station(null)
        );
        assertEquals("Station name cannot be null", ex.getMessage());
    }

}
