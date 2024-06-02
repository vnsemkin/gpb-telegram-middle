package org.vnsemkin.semkinmiddleservice.presentation.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.FrontReqDto;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.FrontRespDto;
import org.vnsemkin.semkinmiddleservice.application.mappers.CustomerMapper;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.application.usecases.CustomerRegistrationService;

@Slf4j
@RestController("/")
@RequiredArgsConstructor
public class AppController {
    public final CustomerRegistrationService customerRegistrationService;
    private final CustomerMapper mapper = CustomerMapper.INSTANCE;

    @PostMapping("registration")
    public ResponseEntity<?> registration(
        @Validated
        @RequestBody FrontReqDto frontReqDto) {
        Result<Customer, String> result = customerRegistrationService.register(frontReqDto);
        return result.isSuccess() ? resultSuccess(result) : resultFail(result);
    }

    private ResponseEntity<FrontRespDto> resultSuccess(@NonNull Result<Customer, String> result) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(mapper.toDto(result.getData().get()));
    }

    private ResponseEntity<String> resultFail(@NonNull Result<Customer, String> result) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(result.getError().get());
    }
}