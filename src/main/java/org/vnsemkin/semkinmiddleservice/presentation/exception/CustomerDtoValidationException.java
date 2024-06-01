package org.vnsemkin.semkinmiddleservice.presentation.exception;

public class CustomerDtoValidationException extends RuntimeException {
    public CustomerDtoValidationException(String message) {
        super(message);
    }
}