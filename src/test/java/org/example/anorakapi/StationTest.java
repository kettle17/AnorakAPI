package org.example.anorakapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class StationTest {

    @MockitoBean
    private StationRepository stationRepository;

    @Test
    @DisplayName("Tests creation of Station object, repository and its methods")
    void testStationCreationAndGetMethods() {
        Station station = new Station("Liverpool Street");
        Station stationWithId = new Station("Liverpool Street");
        stationWithId.setId("mock-id-123");
        when(stationRepository.save(station)).thenReturn(Mono.just(stationWithId));
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
