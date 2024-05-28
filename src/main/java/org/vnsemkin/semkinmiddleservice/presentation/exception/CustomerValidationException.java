package org.vnsemkin.semkinmiddleservice.presentation.exception;

public class CustomerValidationException extends RuntimeException {
    public CustomerValidationException(String message) {
        super(message);
    }
}
