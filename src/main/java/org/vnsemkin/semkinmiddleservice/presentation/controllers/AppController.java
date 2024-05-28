package org.vnsemkin.semkinmiddleservice.presentation.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.vnsemkin.semkinmiddleservice.application.dtos.CustomerDto;
import org.vnsemkin.semkinmiddleservice.application.dtos.ResultDto;
import org.vnsemkin.semkinmiddleservice.application.mappers.CustomerMapper;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.domain.services.CustomerRegistrationService;

@RestController("/")
@RequiredArgsConstructor
public class AppController {
    public final CustomerRegistrationService customerRegistrationService;
    private final CustomerMapper mapper = CustomerMapper.INSTANCE;

    @PostMapping("registration")
    public ResponseEntity<ResultDto<CustomerDto>> registration(@Validated @RequestBody Customer customer) {
        Result<Customer> register = customerRegistrationService
            .register(customer);
        if (register.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResultDto.success(mapper.toDto(register.getData())));
        }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ResultDto.failure(register.getError().getMessage()));
    }
}