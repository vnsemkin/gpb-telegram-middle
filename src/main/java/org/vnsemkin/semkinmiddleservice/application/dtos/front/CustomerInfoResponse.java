package org.vnsemkin.semkinmiddleservice.application.dtos.front;

public record CustomerInfoResponse(String firstName, String username, String email,
                                   String uuid, String accountName) {}
