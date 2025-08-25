package org.example.anorakapi;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ErrorTest {

    @Test
    @DisplayName("Tests creation of Error object and its methods")
    void testErrorCreationAndGetMethods_NoErrorList() {
        Error error = new Error("E001", "Train name cannot be empty");

        assertEquals("E001", error.getErrorCode());
        assertEquals("Train name cannot be empty", error.getErrorMessage());
    }

    @Test
    @DisplayName("Tests creation of Error object and its methods with Error list")
    void testErrorCreationAndGetMethods_WithErrorList() {
        List<Error> errorList = new ArrayList<>();
        Error error2 = new Error("E002", "I am an extra error?");
        errorList.add(error2);
        Error error = new Error("E001", "Train name cannot be empty", errorList);

        assertEquals("E001", error.getErrorCode());
        assertEquals("Train name cannot be empty", error.getErrorMessage());
        assertNotNull(error.getErrors());
    }

    @Test
    @DisplayName("Error should throw exception if code is empty string.")
    void testErrorCreation_ShouldFail_IfCodeEmptyString() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Error("", "Train name cannot be empty")
        );
        assertEquals("Code or message cannot be null or empty", ex.getMessage());
    }

    @Test
    @DisplayName("Error should throw exception if code null.")
    void testErrorCreation_ShouldFail_IfCodeNull() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Error(null, "Train name cannot be empty")
        );
        assertEquals("Code or message cannot be null or empty", ex.getMessage());
    }

    @Test
    @DisplayName("Error should throw exception if message is empty string.")
    void testErrorCreation_ShouldFail_IfMessageEmptyString() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Error("dddd", "")
        );
        assertEquals("Code or message cannot be null or empty", ex.getMessage());
    }

    @Test
    @DisplayName("Error should throw exception if message null.")
    void testErrorCreation_ShouldFail_IfMessageNull() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Error("dddd", null)
        );
        assertEquals("Code or message cannot be null or empty", ex.getMessage());
    }

}
