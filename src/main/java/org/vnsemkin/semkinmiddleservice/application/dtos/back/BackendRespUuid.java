package org.vnsemkin.semkinmiddleservice.application.dtos.back;

import org.vnsemkin.semkinmiddleservice.presentation.exception.UuidValidationException;

public record BackendRespUuid(String uuid) {
    private static final String INVALID_UUID = "Неправильный формат UUID";
    private static final String UUID_REGEX =
        "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    public BackendRespUuid {
        if (!isValidUuid(uuid)) {
            throw new UuidValidationException(INVALID_UUID);
        }
    }

    private boolean isValidUuid(String uuid) {
        return uuid.matches(UUID_REGEX);
    }
}
