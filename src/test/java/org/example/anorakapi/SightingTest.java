package org.example.anorakapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SightingTest {

    @Mock
    private SightingRepository sightingRepository;
    private Train train;
    private Station station;

    @BeforeEach
    void setUp() {
        train = new Train("Henry", "Green", "NWR3");
        station = new Station("Liverpool Street");
    }

    @Test
    @DisplayName("Tests creation of Sighting object and its methods")
    void testSightingCreationAndGetMethods() {
        Sighting sighting = new Sighting(station, train, "2025-08-25T17:35:42.123Z");
        Sighting sightingWithId = new Sighting(station, train, "2025-08-25T17:35:42.123Z");
        sightingWithId.setId("mock-id-123");

        when(sightingRepository.save(sighting)).thenReturn(Mono.just(sightingWithId));

        Sighting savedSighting = sightingRepository.save(sighting).block();

        assertNotNull(savedSighting.getId(), "ID should not be null");
        assertEquals("Liverpool Street", savedSighting.getStation().getName());
        assertEquals("Henry", savedSighting.getTrain().getName());
        assertEquals("2025-08-25T17:35:42.123Z", savedSighting.getTimestamp());
    }

    @Test
    @DisplayName("Sighting should throw exception if timestamp is empty.")
    void testSightingCreation_ShouldFail_IfTimestampEmptyString() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Sighting(station, train, "")
        );
        assertEquals("Timestamp cannot be an empty string", ex.getMessage());
    }

    @Test
    @DisplayName("Sighting should throw exception if timestamp is not correctly formatted.")
    void testSightingCreation_ShouldFail_IfTimestampNotCorrectlyFormatted() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Sighting(station, train, "2025-08-2517:35:00")
        );
        assertEquals("Timestamp is an incorrect format", ex.getMessage());
    }

    @Test
    @DisplayName("Sighting should throw exception if sighting name null.")
    void testSightingCreation_ShouldFail_IfSightingStationNull() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Sighting(null, train, "2025-08-25T17:35:42.123Z")
        );
        assertEquals("Station, Train or Timestamp cannot be null", ex.getMessage());
    }

    @Test
    @DisplayName("Sighting should throw exception if sighting train null.")
    void testSightingCreation_ShouldFail_IfSightingTrainNull() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Sighting(station, null, "2025-08-25T17:35:42.123Z")
        );
        assertEquals("Station, Train or Timestamp cannot be null", ex.getMessage());
    }

    @Test
    @DisplayName("Sighting should throw exception if sighting timestamp null.")
    void testSightingCreation_ShouldFail_IfSightingTimestampNull() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Sighting(station, train, null)
        );
        assertEquals("Station, Train or Timestamp cannot be null", ex.getMessage());
    }

}
