package org.vnsemkin.semkinmiddleservice.domain.models;

import lombok.Getter;
import lombok.NonNull;

import java.util.function.Consumer;
import java.util.function.Function;

@Getter
public final class Result<T> {
    private final T data;
    private final Throwable error;

    private Result(T data, Throwable error) {
        this.data = data;
        this.error = error;
    }

    public static <T> Result<T> success(@NonNull T data) {
        return new Result<>(data, null);
    }

    public static <T> Result<T> failure(@NonNull Throwable error) {
        return new Result<>(null, error);
    }

    public boolean isSuccess() {
        return data != null && error == null;
    }

    public boolean isFailure() {
        return error != null;
    }

    public <U> Result<U> map(Function<? super T, ? extends U> mapper) {
        if (isSuccess()) {
            return Result.success(mapper.apply(data));
        } else {
            return Result.failure(error);
        }
    }

    public void ifSuccess(Consumer<? super T> consumer) {
        if (isSuccess()) {
            consumer.accept(data);
        }
    }

    public void ifFailure(Consumer<? super Throwable> consumer) {
        if (isFailure()) {
            consumer.accept(error);
        }
    }
}
