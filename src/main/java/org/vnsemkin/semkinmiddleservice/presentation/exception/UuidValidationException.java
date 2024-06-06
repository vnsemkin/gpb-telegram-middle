package org.vnsemkin.semkinmiddleservice.presentation.exception;

public class UuidValidationException extends RuntimeException {
    public UuidValidationException(String message) {
        super(message);
    }
}