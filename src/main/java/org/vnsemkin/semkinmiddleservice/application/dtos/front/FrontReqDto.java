package org.vnsemkin.semkinmiddleservice.application.dtos.front;

import org.vnsemkin.semkinmiddleservice.presentation.exception.CustomerDtoValidationException;

public record FrontReqDto(String name, String email, String password) {
    private static final String NAME_REGEX = "^[a-zA-Zа-яА-Я]+$";
    private static final String EMAIL_REGEX = "[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+";

    public FrontReqDto {
        if (!isValidName(name)) {
            throw new CustomerDtoValidationException("Invalid name format or length");
        }
        if (!isValidEmail(email)) {
            throw new CustomerDtoValidationException("Invalid email format or length");
        }
        if (!isValidPassword(password)) {
            throw new CustomerDtoValidationException("Invalid password length");
        }
    }

    private boolean isValidName(String name) {
        return name.matches(NAME_REGEX) && name.length() <= 10;
    }

    private boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX) && email.length() <= 50;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 5 && password.length() <= 10;
    }
}