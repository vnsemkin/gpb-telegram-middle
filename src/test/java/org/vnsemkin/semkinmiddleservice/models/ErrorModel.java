package org.vnsemkin.semkinmiddleservice.models;

public record ErrorModel(String message, String type, String code, String trace_id) {
}
