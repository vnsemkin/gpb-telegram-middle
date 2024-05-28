package org.vnsemkin.semkinmiddleservice.domain.models;

import org.vnsemkin.semkinmiddleservice.presentation.exception.CustomerValidationException;

public record Customer(String name, String email, String password) {
    public Customer {
        if (!isValidName(name)) {
            throw new CustomerValidationException("Invalid name format or length");
        }
        if (!isValidEmail(email)) {
            throw new CustomerValidationException("Invalid email format or length");
        }
        if (!isValidPassword(password)) {
            throw new CustomerValidationException("Invalid password length");
        }
    }

    private boolean isValidName(String name) {
        return name.matches("^[a-zA-Zа-яА-Я]+$") && name.length() <= 10;
    }

    private boolean isValidEmail(String email) {
        return email.matches("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+") && email.length() <= 50;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 5 && password.length() <= 10;
    }
}
