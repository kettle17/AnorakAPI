package org.example.anorakapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TrainTest {

    @MockitoBean
    private TrainRepository trainRepository;

    @Test
    @DisplayName("Tests creation of Train object, repository and its methods")
    void testTrainCreationAndGetMethods() {
        Train train = new Train("Henry", "Green", "NWR3");
        Train savedTrainWithId = new Train("Henry", "Green", "NWR3");
        savedTrainWithId.setId("mock-id-123");
        when(trainRepository.save(train)).thenReturn(Mono.just(savedTrainWithId));
        Train savedTrain = trainRepository.save(train).block();

        assertNotNull(savedTrain.getId(), "ID should not be null");
        assertEquals("Henry", train.getName());
        assertEquals("Green", train.getColour());
        assertEquals("NWR3", train.getTrainNumber());
    }

    @Test
    @DisplayName("Train should throw exception if name is too short.")
    void testTrainCreation_ShouldFail_IfNameTooShort() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Train("A", "Green", "NWR3")
        );
        assertEquals("Train name must be between 2 and 100 characters", ex.getMessage());
    }

    @Test
    @DisplayName("Train should throw exception if name is too long.")
    void testTrainCreation_ShouldFail_IfNameTooLong() {
        String longName = "A".repeat(101);
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Train(longName, "Green", "NWR3")
        );
        assertEquals("Train name must be between 2 and 100 characters", ex.getMessage());
    }

    @Test
    @DisplayName("Train should throw exception if colour empty string.")
    void testTrainCreation_ShouldFail_IfColourEmptyString() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Train("Hello", "", "NWR3")
        );
        assertEquals("Train colour or number cannot be empty", ex.getMessage());
    }

    @Test
    @DisplayName("Train should throw exception if train number empty string.")
    void testTrainCreation_ShouldFail_IfTrainNumEmptyString() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Train("Hello", "Blue", "")
        );
        assertEquals("Train colour or number cannot be empty", ex.getMessage());
    }

    @Test
    @DisplayName("Train should throw exception if train name null.")
    void testTrainCreation_ShouldFail_IfTrainNameNull() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Train(null, "Blue", "E2FF")
        );
        assertEquals("Train name, colour, or number cannot be null", ex.getMessage());
    }

    @Test
    @DisplayName("Train should throw exception if train colour null.")
    void testTrainCreation_ShouldFail_IfTrainColourNull() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Train("Jerry", null, "E2FF")
        );
        assertEquals("Train name, colour, or number cannot be null", ex.getMessage());
    }

    @Test
    @DisplayName("Train should throw exception if train number null.")
    void testTrainCreation_ShouldFail_IfTrainNumNull() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Train("Jerry", "Turquoise", null)
        );
        assertEquals("Train name, colour, or number cannot be null", ex.getMessage());
    }
}
