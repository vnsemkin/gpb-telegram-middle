package org.vnsemkin.semkinmiddleservice.models;

import org.junit.jupiter.api.Test;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.FrontReqDto;
import org.vnsemkin.semkinmiddleservice.presentation.exception.CustomerDtoValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FrontReqDtoTest {
    private final static long TG_USER_ID = 137264783L;
    private final static String TG_USERNAME = "Test";
    private final static String FIRST_NAME = "John";
    private final static String VALID_EMAIL = "john@example.com";
    private final static String INVALID_EMAIL = "invalid-email";
    private final static String VALID_PASSWORD = "password";
    private final static String INVALID_PASSWORD = "test";

    @Test
    public void whenValidNameEmailAndPassword_thenCreateCustomerReqDto() {
        // ARRANGE
        // ACT
        FrontReqDto frontReqDto = new FrontReqDto(TG_USER_ID,
            FIRST_NAME, TG_USERNAME, VALID_EMAIL, VALID_PASSWORD);
        // ASSERT
        assertEquals(VALID_EMAIL, frontReqDto.email());
        assertEquals(VALID_PASSWORD, frontReqDto.password());
    }

    @Test
    public void whenInvalidEmail_thenThrowCustomerValidationException() {
        // ARRANGE
        // ACT
        CustomerDtoValidationException exception = assertThrows(
            CustomerDtoValidationException.class,
            () -> new FrontReqDto(TG_USER_ID,
                FIRST_NAME,TG_USERNAME,INVALID_EMAIL, VALID_PASSWORD)
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
            () -> new FrontReqDto(TG_USER_ID,
                FIRST_NAME,TG_USERNAME,INVALID_EMAIL, INVALID_PASSWORD)
        );
        // ASSERT
        assertEquals("Invalid email format or length", exception.getMessage());
    }
}