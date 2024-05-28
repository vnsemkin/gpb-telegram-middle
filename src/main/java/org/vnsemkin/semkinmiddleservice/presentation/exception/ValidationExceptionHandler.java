package org.vnsemkin.semkinmiddleservice.presentation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.vnsemkin.semkinmiddleservice.application.dtos.ResultDto;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(CustomerValidationException.class)
    public ResponseEntity<ResultDto<Void>> handleValidationExceptions(CustomerValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultDto.failure(ex.getMessage()));
    }
}
