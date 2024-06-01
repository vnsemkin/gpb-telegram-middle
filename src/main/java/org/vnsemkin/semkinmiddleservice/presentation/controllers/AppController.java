package org.vnsemkin.semkinmiddleservice.presentation.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerReqDto;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerRespDto;
import org.vnsemkin.semkinmiddleservice.application.dtos.ResultDto;
import org.vnsemkin.semkinmiddleservice.application.mappers.CustomerMapper;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.domain.services.CustomerRegistrationService;

@Slf4j
@RestController("/")
@RequiredArgsConstructor
public class AppController {
    public final CustomerRegistrationService customerRegistrationService;
    private final CustomerMapper mapper = CustomerMapper.INSTANCE;

    @PostMapping("registration")
    public ResponseEntity<ResultDto<CustomerRespDto>> registration(
        @Validated
        @RequestBody CustomerReqDto customerReqDto) {
        Result<Customer> result = customerRegistrationService.register(customerReqDto);
        return result.isSuccess() ? resultSuccess(result) : resultFails(result);
    }

    private ResponseEntity<ResultDto<CustomerRespDto>> resultSuccess(Result<Customer> result) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ResultDto.success(mapper.toDto(result.getData().get())));
    }

    private ResponseEntity<ResultDto<CustomerRespDto>> resultFails(Result<Customer> result) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResultDto.failure(result.getError().get().getMessage()));
    }
}