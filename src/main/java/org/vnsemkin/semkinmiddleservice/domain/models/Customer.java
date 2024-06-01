package org.vnsemkin.semkinmiddleservice.domain.models;

public record Customer(long id, String name, String email, String password, String uuid) {
}
