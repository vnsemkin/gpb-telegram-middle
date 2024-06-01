package org.vnsemkin.semkinmiddleservice.application.dtos;

import lombok.Getter;
import lombok.NonNull;

import java.util.Optional;

@Getter
public class ResultDto<T> {
    private final T data;
    private final String error;

    private ResultDto(T data, String error) {
        this.data = data;
        this.error = error;
    }

    public static <T> ResultDto<T> success(@NonNull T data) {
        return new ResultDto<>(data, null);
    }

    public static <T> ResultDto<T> failure(@NonNull String error) {
        return new ResultDto<>(null, error);
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

    public Optional<String> getError() {
        return isFailure() ? Optional.of(error) : Optional.empty();
    }
}
