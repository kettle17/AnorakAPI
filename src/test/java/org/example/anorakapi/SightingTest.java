package org.example.anorakapi;

import com.google.cloud.firestore.DocumentReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class SightingTest {

    private DocumentReference trainRef;
    private DocumentReference stationRef;

    @BeforeEach
    void setUp() {
        trainRef = mock(DocumentReference.class);
        stationRef = mock(DocumentReference.class);
    }

    @Test
    @DisplayName("Tests creation of Sighting object and its getters")
    void testSightingCreationAndGetMethods() {
        String timestamp = "2025-08-25T17:35:42.123Z";
        Sighting sighting = new Sighting(stationRef, trainRef, timestamp);
        sighting.setId("mock-id-123");

        assertEquals("mock-id-123", sighting.getId());
        assertEquals(stationRef, sighting.getStation());
        assertEquals(trainRef, sighting.getTrain());
        assertEquals(timestamp, sighting.getTimestamp());
    }

    @Test
    @DisplayName("Sighting should throw exception if timestamp is empty")
    void testSighting_ShouldFail_IfTimestampEmpty() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Sighting(stationRef, trainRef, "")
        );
        assertEquals("Timestamp cannot be an empty string", ex.getMessage());
    }

    @Test
    @DisplayName("Sighting should throw exception if timestamp is invalid format")
    void testSighting_ShouldFail_IfTimestampInvalidFormat() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Sighting(stationRef, trainRef, "2025-08-2517:35:00")
        );
        assertEquals("Timestamp is an incorrect format", ex.getMessage());
    }

    @Test
    @DisplayName("Sighting should throw exception if station is null")
    void testSighting_ShouldFail_IfStationNull() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Sighting(null, trainRef, "2025-08-25T17:35:42.123Z")
        );
        assertEquals("Station, Train or Timestamp cannot be null", ex.getMessage());
    }

    @Test
    @DisplayName("Sighting should throw exception if train is null")
    void testSighting_ShouldFail_IfTrainNull() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Sighting(stationRef, null, "2025-08-25T17:35:42.123Z")
        );
        assertEquals("Station, Train or Timestamp cannot be null", ex.getMessage());
    }

    @Test
    @DisplayName("Sighting should throw exception if timestamp is null")
    void testSighting_ShouldFail_IfTimestampNull() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Sighting(stationRef, trainRef, null)
        );
        assertEquals("Station, Train or Timestamp cannot be null", ex.getMessage());
    }

}
