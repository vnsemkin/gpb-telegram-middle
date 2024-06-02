package org.vnsemkin.semkinmiddleservice.application.dtos.back;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record BackendErrorResponse(
    String message,
    String type,
    String code,
    String traceId
) {
    @JsonCreator
    public BackendErrorResponse(
        @JsonProperty("message") String message,
        @JsonProperty("type") String type,
        @JsonProperty("code") String code,
        @JsonProperty("trace_id") String traceId
    ) {
        this.message = message;
        this.type = type;
        this.code = code;
        this.traceId = traceId;
    }
}