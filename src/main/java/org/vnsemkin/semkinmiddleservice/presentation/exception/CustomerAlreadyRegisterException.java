package org.vnsemkin.semkinmiddleservice.presentation.exception;

public class CustomerAlreadyRegisterException extends RuntimeException{
    public CustomerAlreadyRegisterException(String message) {
        super(message);
    }
}
