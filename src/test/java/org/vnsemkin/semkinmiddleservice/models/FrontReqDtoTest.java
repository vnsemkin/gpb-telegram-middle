package org.vnsemkin.semkinmiddleservice.models;

import org.junit.jupiter.api.Test;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.FrontReqDto;
import org.vnsemkin.semkinmiddleservice.presentation.exception.CustomerDtoValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FrontReqDtoTest {
    private final static String VALID_NAME = "John";
    private final static String INVALID_NAME = "Invalid123";
    private final static String VALID_EMAIL = "john@example.com";
    private final static String INVALID_EMAIL = "invalid-email";
    private final static String VALID_PASSWORD = "password";
    private final static String INVALID_PASSWORD = "test";

    @Test
    public void whenValidNameEmailAndPassword_thenCreateCustomerReqDto() {
        // ARRANGE
        // ACT
        FrontReqDto frontReqDto = new FrontReqDto(VALID_NAME, VALID_EMAIL, VALID_PASSWORD);
        // ASSERT
        assertEquals(VALID_NAME, frontReqDto.name());
        assertEquals(VALID_EMAIL, frontReqDto.email());
        assertEquals(VALID_PASSWORD, frontReqDto.password());
    }

    @Test
    public void whenInvalidName_thenThrowCustomerValidationException() {
        // ARRANGE
        // ACT
        CustomerDtoValidationException exception = assertThrows(
            CustomerDtoValidationException.class,
            () -> new FrontReqDto(INVALID_NAME, VALID_EMAIL, VALID_PASSWORD)
        );
        // ASSERT
        assertEquals("Invalid name format or length", exception.getMessage());
    }

    @Test
    public void whenInvalidEmail_thenThrowCustomerValidationException() {
        // ARRANGE
        // ACT
        CustomerDtoValidationException exception = assertThrows(
            CustomerDtoValidationException.class,
            () -> new FrontReqDto(VALID_NAME, INVALID_EMAIL, VALID_PASSWORD)
        );
        // ASSERT
        assertEquals("Invalid email format or length", exception.getMessage());
    }

    @Test
    public void whenInvalidPassword_thenThrowCustomerValidationException() {
        // ARRANGE
        // ACT
        CustomerDtoValidationException exception = assertThrows(
            CustomerDtoValidationException.class,
            () -> new FrontReqDto(VALID_NAME, VALID_EMAIL, INVALID_PASSWORD)
        );
        // ASSERT
        assertEquals("Invalid password length", exception.getMessage());
    }
}