package org.vnsemkin.semkinmiddleservice.models;

import org.junit.jupiter.api.Test;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.presentation.exception.CustomerDtoValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerTest {
    private final static String VALID_NAME = "John";
    private final static String INVALID_NAME = "Invalid123";
    private final static String VALID_EMAIL = "john@example.com";
    private final static String INVALID_EMAIL = "invalid-email";
    private final static String VALID_PASSWORD = "password";
    private final static String INVALID_PASSWORD = "test";
    private final static String VALID_UUID = "123e4567-e89b-12d3-a456-426614174000";
    private final static String INVALID_UUID = "invalid-uuid";

    @Test
    public void whenInvalidName_thenThrowCustomerValidationException() {
        // ARRANGE
        // ACT
        CustomerDtoValidationException exception = assertThrows(
            CustomerDtoValidationException.class,
            () -> new Customer(1L, INVALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_UUID)
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
            () -> new Customer(1L, VALID_NAME, INVALID_EMAIL, VALID_PASSWORD, VALID_UUID)
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
            () -> new Customer(1L, VALID_NAME, VALID_EMAIL, INVALID_PASSWORD, VALID_UUID)
        );
        // ASSERT
        assertEquals("Invalid password length", exception.getMessage());
    }

    @Test
    public void whenInvalidUuid_thenThrowCustomerValidationException() {
        // ARRANGE
        // ACT
        CustomerDtoValidationException exception = assertThrows(
            CustomerDtoValidationException.class,
            () -> new Customer(1L, VALID_NAME, VALID_EMAIL, VALID_PASSWORD, INVALID_UUID)
        );
        // ASSERT
        assertEquals("Invalid uuid format", exception.getMessage());
    }

    @Test
    public void whenValidFields_thenCreateCustomer() {
        // ARRANGE
        // ACT
        Customer customer = new Customer(1L, VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_UUID);
        // ASSERT
        assertNotNull(customer);
        assertEquals(VALID_NAME, customer.name());
        assertEquals(VALID_EMAIL, customer.email());
        assertEquals(VALID_PASSWORD, customer.password());
        assertEquals(VALID_UUID, customer.uuid());
    }
}

