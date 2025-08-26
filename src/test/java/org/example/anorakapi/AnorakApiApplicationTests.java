package org.example.anorakapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureMockMvc
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

    @DisplayName("POST /sightings, successful response will respond with a 201 CREATED")
    @Test
    public void testSightings_SuccessfulPost_Returns201() throws Exception {
        String json = """
        [
          {
            "train": { "name": "Express 1", "colour": "Red", "trainNumber": "12345" },
            "station": { "name": "London Euston" },
            "timestamp": "2025-08-25T17:35:42Z"
          }
        ]
        """;

        Train train = new Train("Express 1", "Red", "12345");
        Station station = new Station("London Euston");
        Sighting sighting = new Sighting();
        sighting.setTrain(train);
        sighting.setStation(station);
        sighting.setTimestamp("2025-08-25T17:35:42Z");

        when(anorakApiService.saveSightings(org.mockito.ArgumentMatchers.anyList()))
                .thenReturn(List.of(sighting));

        mvc.perform(post("/sightings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sightings[0].train.name").value("Express 1"))
                .andExpect(jsonPath("$.sightings[0].train.colour").value("Red"))
                .andExpect(jsonPath("$.sightings[0].train.trainNumber").value("12345"))
                .andExpect(jsonPath("$.sightings[0].station.name").value("London Euston"))
                .andExpect(jsonPath("$.sightings[0].timestamp").value("2025-08-25T17:35:42Z"));
    }

    @DisplayName("POST /sightings, invalid JSON should return 400")
    @Test
    public void testSightings_BadPost_Returns400() throws Exception {
        String json = """
    [
      {
        "train": { "name": "", "colour": "Red", "trainNumber": "12345" },
        "station": { "name": "" },
        "timestamp": ""
      }
    ]
    """;

        mvc.perform(post("/sightings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("E400"))
                .andExpect(jsonPath("$.message").exists());
    }

    @DisplayName("POST /sightings, one valid and one failing sighting should return 500")
    @Test
    public void testSightings_PartialFailure_Returns500() throws Exception {
        String json = """
    [
      {
        "train": { "name": "Express 1", "colour": "Red", "trainNumber": "12345" },
        "station": { "name": "London Euston" },
        "timestamp": "2025-08-25T17:35:42Z"
      },
      {
        "train": { "name": "Express 2", "colour": "Blue", "trainNumber": "67890" },
        "station": { "name": "King's Cross" },
        "timestamp": "2025-08-25T18:00:00Z"
      }
    ]
    """;

        Train train1 = new Train("Express 1", "Red", "12345");
        Station station1 = new Station("London Euston");
        Sighting sighting1 = new Sighting();
        sighting1.setTrain(train1);
        sighting1.setStation(station1);
        sighting1.setTimestamp("2025-08-25T17:35:42Z");

        when(anorakApiService.saveSightings(org.mockito.ArgumentMatchers.anyList()))
                .thenThrow(new ErrorException("E500", "Some sightings could not be saved",
                        List.of("Sighting 2: Failed to save: Simulated failure"), HttpStatus.INTERNAL_SERVER_ERROR));

        mvc.perform(post("/sightings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("E500"))
                .andExpect(jsonPath("$.errors[0]").value("Sighting 2: Failed to save: Simulated failure"));
    }

    @DisplayName("POST /sightings, multiple sightings return 201 CREATED")
    @Test
    public void testSightings_Multiple_Returns201() throws Exception {
        String json = """
            [
              {
                "train": { "name": "Express 1", "colour": "Red", "trainNumber": "12345" },
                "station": { "name": "London Euston" },
                "timestamp": "2025-08-25T17:35:42Z"
              },
              {
                "train": { "name": "Express 2", "colour": "Blue", "trainNumber": "54321" },
                "station": { "name": "Manchester Piccadilly" },
                "timestamp": "2025-08-25T18:00:00Z"
              }
            ]
        """;

        Train t1 = new Train("Express 1", "Red", "12345");
        Station s1 = new Station("London Euston");
        Sighting sighting1 = new Sighting();
        sighting1.setTrain(t1);
        sighting1.setStation(s1);
        sighting1.setTimestamp("2025-08-25T17:35:42Z");

        Train t2 = new Train("Express 2", "Blue", "54321");
        Station s2 = new Station("Manchester Piccadilly");
        Sighting sighting2 = new Sighting();
        sighting2.setTrain(t2);
        sighting2.setStation(s2);
        sighting2.setTimestamp("2025-08-25T18:00:00Z");

        when(anorakApiService.saveSightings(org.mockito.ArgumentMatchers.anyList()))
                .thenReturn(List.of(sighting1, sighting2));

        mvc.perform(post("/sightings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sightings[0].train.name").value("Express 1"))
                .andExpect(jsonPath("$.sightings[1].train.name").value("Express 2"))
                .andExpect(jsonPath("$.sightings[0].station.name").value("London Euston"))
                .andExpect(jsonPath("$.sightings[1].station.name").value("Manchester Piccadilly"));
    }

}
