package org.example.anorakapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class AnorakApiApplicationTests {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AnorakApiService anorakApiService;

    //Train Tests

    @DisplayName("GET /train should return 200 when populated in expected format")
    @Test
    public void testTrainsReturns200() throws Exception {
        List<Train> trains = new ArrayList<>();
        Train train1 = new Train("Carl","Blue", "LE-01");
        Train train2 = new Train("Jim","Red", "MR-23");
        trains.add(train1);
        trains.add(train2);
        when(anorakApiService.getAllTrains()).thenReturn(trains);

        mvc.perform(get("/train")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trains[*].name").isNotEmpty())
                .andExpect(jsonPath("$.trains[*].colour").isNotEmpty())
                .andExpect(jsonPath("$.trains[*].trainNumber").isNotEmpty());
    }

    @DisplayName("GET /train should return 200 and 'trains' even if empty")
    @Test
    public void testTrainsEmptyReturns200() throws Exception {
        List<Train> trains = new ArrayList<>();
        when(anorakApiService.getAllTrains()).thenReturn(trains);

        mvc.perform(get("/train")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trains").isArray())
                .andExpect(jsonPath("$.trains").isEmpty());
    }

    //Train/{id} tests

    @DisplayName("GET /train/{id}, populating and calling should return 200")
    @Test
    public void testTrain_GetID_Returns200() throws Exception {
        Train train1 = new Train("Carl","Blue", "LE-01");
        String id = "123"; // mock id
        train1.setId(id);
        when(anorakApiService.getTrainById(id)).thenReturn(train1);

        mvc.perform(get("/train/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Carl"))
                .andExpect(jsonPath("$.colour").value("Blue"))
                .andExpect(jsonPath("$.trainNumber").value("LE-01"));
    }

    @DisplayName("GET /train/{id}, calling wrong id should return 404")
    @Test
    public void testTrain_GetID_NotFound_Returns404() throws Exception {
        String badId = "does-not-exist";

        when(anorakApiService.getTrainById(badId))
                .thenThrow(new ErrorException("E001", "Train not found", Collections.emptyList(), HttpStatus.NOT_FOUND));

        mvc.perform(get("/train/" + badId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("E001"))
                .andExpect(jsonPath("$.message").value("Train not found"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    //Train/{id}/sighting tests
    @DisplayName("GET /train/{id}/sighting, populating and calling should return 200.")
    @Test
    public void testTrain_GetID_GetSighting_Returns200() throws Exception {
        Train train = new Train("Thomas", "Blue", "T1192A");
        train.setId("train-123");
        Station station = new Station("Liverpool Street");
        station.setId("station-456");
        Sighting sighting = new Sighting(station, train, "2025-08-25T10:15:30Z");
        List<Sighting> sightings = List.of(sighting);

        when(anorakApiService.getSightingsByTrainId("train-123")).thenReturn(sightings);

        mvc.perform(get("/train/train-123/sightings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sightings[0].train.id").value("train-123"))
                .andExpect(jsonPath("$.sightings[0].train.name").value("Thomas"))
                .andExpect(jsonPath("$.sightings[0].train.colour").value("Blue"))
                .andExpect(jsonPath("$.sightings[0].train.trainNumber").value("T1192A"))
                .andExpect(jsonPath("$.sightings[0].station.id").value("station-456"))
                .andExpect(jsonPath("$.sightings[0].station.name").value("Liverpool Street"))
                .andExpect(jsonPath("$.sightings[0].timestamp").value("2025-08-25T10:15:30Z"));
    }

    @DisplayName("GET /train/{id}/sighting, populating and calling should return 200 even if result is empty.")
    @Test
    public void testTrain_GetID_GetSighting_Returns200_EvenWhenNoResult() throws Exception {
        when(anorakApiService.getSightingsByTrainId("train-123")).thenReturn(Collections.emptyList());

        mvc.perform(get("/train/train-123/sightings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sightings").isArray())
                .andExpect(jsonPath("$.sightings").isEmpty());
    }



}
