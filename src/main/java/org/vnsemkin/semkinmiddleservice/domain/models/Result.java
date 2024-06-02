package org.vnsemkin.semkinmiddleservice.domain.models;

import lombok.NonNull;

import java.util.Optional;

public final class Result<T, E> {
    private final T data;
    private final E error;

    private Result(T data, E error) {
        this.data = data;
        this.error = error;
    }

    public static <T, E> Result<T, E> success(@NonNull T data) {
        return new Result<>(data, null);
    }

    public static <T, E> Result<T, E> error(@NonNull E error) {
        return new Result<>(null, error);
    }

    public boolean isSuccess() {
        return data != null && error == null;
    }

    public boolean isError() {
        return error != null && data == null;
    }

    public Optional<T> getData() {
        return isSuccess() ? Optional.of(data) : Optional.empty();
    }

    public Optional<E> getError() {
        return isError() ? Optional.of(error) : Optional.empty();
    }
}