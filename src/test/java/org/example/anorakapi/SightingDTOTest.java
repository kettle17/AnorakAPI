package org.example.anorakapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class SightingDTOTest {

    @Mock
    private Train train;
    private Station station;

    @BeforeEach
    void setUp() {
        train = new Train("Henry", "Green", "NWR3");
        station = new Station("Liverpool Street");
    }

    @Test
    @DisplayName("Tests creation of Sighting DTO object and its methods")
    void testSightingDTO_CreationAndGetMethods() {
        SightingDTO dto = new SightingDTO(station, train, "2025-08-25T17:35:42.123Z");
        dto.setId("mock-id-123");

        assertEquals("mock-id-123", dto.getId());
        assertEquals("Liverpool Street", dto.getStation().getName());
        assertEquals("Henry", dto.getTrain().getName());
        assertEquals("2025-08-25T17:35:42.123Z", dto.getTimestamp());
    }

    @Test
    @DisplayName("SightingDTO should throw exception if timestamp is empty")
    void testSightingDTO_ShouldFail_IfTimestampEmpty() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new SightingDTO(station, train, "")
        );
        assertEquals("Timestamp cannot be an empty string", ex.getMessage());
    }

    @Test
    @DisplayName("SightingDTO should throw exception if timestamp is not correctly formatted")
    void testSightingDTO_ShouldFail_IfTimestampInvalidFormat() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new SightingDTO(station, train, "2025-08-2517:35:00")
        );
        assertEquals("Timestamp is an incorrect format", ex.getMessage());
    }

    @Test
    @DisplayName("SightingDTO should throw exception if station is null")
    void testSightingDTO_ShouldFail_IfStationNull() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new SightingDTO(null, train, "2025-08-25T17:35:42.123Z")
        );
        assertEquals("Station, Train or Timestamp cannot be null", ex.getMessage());
    }

    @Test
    @DisplayName("SightingDTO should throw exception if train is null")
    void testSightingDTO_ShouldFail_IfTrainNull() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new SightingDTO(station, null, "2025-08-25T17:35:42.123Z")
        );
        assertEquals("Station, Train or Timestamp cannot be null", ex.getMessage());
    }

    @Test
    @DisplayName("SightingDTO should throw exception if timestamp is null")
    void testSightingDTO_ShouldFail_IfTimestampNull() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new SightingDTO(station, train, null)
        );
        assertEquals("Station, Train or Timestamp cannot be null", ex.getMessage());
    }


}
