package org.vnsemkin.semkinmiddleservice.domain.models;

public record Customer(long id, long tgId, String firstName, String username, String email, String passwordHash,
                       String uuid) {
}