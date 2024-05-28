package org.vnsemkin.semkinmiddleservice.application.dtos;

import lombok.Getter;
import lombok.NonNull;

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
}
