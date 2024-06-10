package org.vnsemkin.semkinmiddleservice.application.dtos.front;

import org.vnsemkin.semkinmiddleservice.presentation.exception.CustomerDtoValidationException;

import static org.vnsemkin.semkinmiddleservice.application.config.AppConstants.*;

public record CustomerRegistrationRequest(long tgId, String firstName, String username, String email, String password) {
    private static final String EMAIL_REGEX = "[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+";

    public CustomerRegistrationRequest {
        if (!isValidEmail(email)) {
            throw new CustomerDtoValidationException("Invalid email format or length");
        }
        if (!isValidPassword(password)) {
            throw new CustomerDtoValidationException("Invalid password length");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX) && email.length() <= MAX_EMAIL_LENGTH;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= MIN_PASSWORD_LENGTH && password.length() <= MAX_PASSWORD_LENGTH;
    }
}