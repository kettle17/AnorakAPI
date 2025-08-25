package org.example.anorakapi;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TrainTest {

    @Autowired
    private EntityManager entityManager; //stand-in for repository

    @Test
    @DisplayName("Tests creation of Train object and its methods")
    void testTrainCreationAndGetMethods() {
        //Using UUID as a standin for auto-generated ID
        Train train = new Train("Henry", "Green", "NWR3");

        entityManager.persist(train);
        entityManager.flush();
        assertNotNull(train.getId(), "ID should not be null");
        assertDoesNotThrow(() -> UUID.fromString(train.getId()));

        assertEquals("Henry", train.getName());
        assertEquals("Green", train.getColour());
        assertEquals("NWR3", train.getTrainNumber());
    }

    @Test
    @DisplayName("Train should throw exception if name is too short.")
    void testTrainCreation_ShouldFail_IfNameTooShort() {
        Exception shortNameException = assertThrows(
                IllegalArgumentException.class,
                () -> new Train("A", "Green", "NWR3")
        );
        assertEquals("Train name must be between 2 and 100 characters", shortNameException.getMessage());
    }

    @Test
    @DisplayName("Train should throw exception if name is too long.")
    void testTrainCreation_ShouldFail_IfNameTooLong() {
        String longName = "A".repeat(101);
        Exception longNameException = assertThrows(
                IllegalArgumentException.class,
                () -> new Train(longName, "Green", "NWR3")
        );
        assertEquals("Train name must be between 2 and 100 characters", longNameException.getMessage());
    }

    @Test
    @DisplayName("Train should throw exception if colour empty string.")
    void testTrainCreation_ShouldFail_IfColourEmptyString() {
        Exception shortNameException = assertThrows(
                IllegalArgumentException.class,
                () -> new Train("Hello", "", "NWR3")
        );
        assertEquals("Train colour or number cannot be empty", shortNameException.getMessage());
    }

    @Test
    @DisplayName("Train should throw exception if train number empty string.")
    void testTrainCreation_ShouldFail_IfTrainNumEmptyString() {
        Exception shortNameException = assertThrows(
                IllegalArgumentException.class,
                () -> new Train("Hello", "Blue", "")
        );
        assertEquals("Train colour or number cannot be empty", shortNameException.getMessage());
    }

}
