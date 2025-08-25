package org.example.anorakapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
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

}
