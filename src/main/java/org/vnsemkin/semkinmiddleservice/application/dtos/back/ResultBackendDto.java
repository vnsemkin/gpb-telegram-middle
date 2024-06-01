package org.vnsemkin.semkinmiddleservice.application.dtos.back;

import lombok.NonNull;
import lombok.ToString;

import java.util.Optional;

@ToString
public class ResultBackendDto<T> {
    private final T data;
    private final BackendErrorResponse error;

    private ResultBackendDto(T data, BackendErrorResponse error) {
        this.data = data;
        this.error = error;
    }

    public static <T> ResultBackendDto<T> success(@NonNull T data) {
        return new ResultBackendDto<>(data, null);
    }

    public static <T> ResultBackendDto<T> failure(@NonNull BackendErrorResponse error) {
        return new ResultBackendDto<>(null, error);
    }

    public boolean isSuccess() {
        return data != null && error == null;
    }

    public boolean isFailure() {
        return error != null;
    }

    public Optional<T> getData() {
        return isSuccess() ? Optional.of(data) : Optional.empty();
    }

    public Optional<BackendErrorResponse> getError() {
        return isFailure() ? Optional.of(error) : Optional.empty();
    }
}
