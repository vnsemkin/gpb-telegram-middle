package org.vnsemkin.semkinmiddleservice.presentation.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.AccountRegistrationResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerInfoResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerRegistrationRequest;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerRegistrationResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.TransferRequest;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.TransferResponse;
import org.vnsemkin.semkinmiddleservice.application.mappers.AppMapper;
import org.vnsemkin.semkinmiddleservice.application.usecases.CustomerRegistrationService;
import org.vnsemkin.semkinmiddleservice.domain.models.Account;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.domain.services.AccountService;
import org.vnsemkin.semkinmiddleservice.domain.services.CustomerService;
import org.vnsemkin.semkinmiddleservice.domain.services.TransferService;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public final class AppController {
    private final static long NEGATIVE_LONG = -1L;
    private final static String UNKNOWN_ERROR = "Неизвестная ошибка";
    private final static String WELCOME_MESSAGE = "Спасибо, что пользуетесь нашим банком";
    private final static String REGISTRATION_BONUS = "Вам начислили 5000 руб за регистрацию в подарок";
    private final static String EMPTY_STRING = "";
    private final AccountService accountService;
    private final CustomerRegistrationService customerRegistrationService;
    private final AppMapper mapper = AppMapper.INSTANCE;
    private final CustomerService customerService;
    private final TransferService transferService;


    @PostMapping("customers")
    public ResponseEntity<?> customerRegistration(
        @Validated
        @RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        Result<Customer, String> customerRegistration =
            customerRegistrationService.register(customerRegistrationRequest);
        return customerRegistration.isSuccess() ? customerRegistrationSuccess(customerRegistration) :
            customerRegistrationFail(customerRegistration);
    }

    @GetMapping("customers/{id}")
    public ResponseEntity<?> customersInfo(@PathVariable long id) {
        Result<CustomerInfoResponse, String> customerInfo = customerService.getCustomerInfo(id);
        return customerInfo.isSuccess() ? getCustomerInfo(customerInfo) : getCustomerInfoError(customerInfo);
    }

    private ResponseEntity<CustomerInfoResponse> getCustomerInfo(Result<CustomerInfoResponse, String> customerInfo) {
        return ResponseEntity.ok().body(customerInfo.getData()
            .orElse(new CustomerInfoResponse(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING)));
    }

    private ResponseEntity<String> getCustomerInfoError(Result<CustomerInfoResponse, String> customerInfo) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(customerInfo.getError().orElse(UNKNOWN_ERROR));
    }

    private ResponseEntity<CustomerRegistrationResponse> customerRegistrationSuccess(
        @NonNull Result<Customer, String> result) {
        return result.getData()
            .map(customer -> ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toCustomerRegistrationResponse(customer)))
            .orElse(ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new CustomerRegistrationResponse(EMPTY_STRING, EMPTY_STRING)));
    }

    private ResponseEntity<String> customerRegistrationFail
        (@NonNull Result<Customer, String> result) {
        return result.getError()
            .map(error -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error))
            .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UNKNOWN_ERROR));
    }

    @PostMapping("customers/{id}/accounts")
    public ResponseEntity<?> accountRegistration(@PathVariable long id) {
        Result<Account, String> accountRegistration = accountService.createAccount(id);
        return accountRegistration.isSuccess() ? accountRegistrationSuccess(accountRegistration) :
            accountRegistrationFail(accountRegistration);
    }

    private ResponseEntity<AccountRegistrationResponse> accountRegistrationSuccess(
        @NonNull Result<Account, String> result) {
        return result.getData()
            .map(account -> ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toAccountRegistrationResponse(account, REGISTRATION_BONUS)))
            .orElse(ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new AccountRegistrationResponse(EMPTY_STRING, new BigDecimal(0), WELCOME_MESSAGE)));
    }

    private ResponseEntity<String> accountRegistrationFail
        (@NonNull Result<Account, String> result) {
        return result.getError()
            .map(error -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error))
            .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UNKNOWN_ERROR));
    }

    @GetMapping("customers/{id}/accounts")
    public ResponseEntity<?> getCustomerAccount(@PathVariable long id) {
        Result<Account, String> accountRegistration = accountService.getAccount(id);
        return accountRegistration.isSuccess() ? getCustomerAccountInfo(accountRegistration) :
            getCustomerAccountError(accountRegistration);
    }

    private ResponseEntity<Account> getCustomerAccountInfo(Result<Account, String> accountRegistration) {
        return accountRegistration.getData()
            .map(account -> ResponseEntity.ok().body(account)).orElse(ResponseEntity.status(HttpStatus
                    .INTERNAL_SERVER_ERROR)
                .body(new Account(NEGATIVE_LONG, EMPTY_STRING, EMPTY_STRING, new BigDecimal(NEGATIVE_LONG))));
    }

    private ResponseEntity<String> getCustomerAccountError(Result<Account, String> accountRegistration) {
        return accountRegistration.getError()
            .map(error -> ResponseEntity.badRequest().body(error)).orElse(ResponseEntity.status(HttpStatus
                .INTERNAL_SERVER_ERROR).body(UNKNOWN_ERROR));
    }

    @PostMapping("transfers")
    public ResponseEntity<?> transfers(@RequestBody TransferRequest request) {
        Result<TransferResponse, String> transfer = transferService.transfer(request);
        return transfer.isSuccess() ? transferSuccess(transfer) : transferFail(transfer);
    }

    private ResponseEntity<?> transferSuccess(Result<TransferResponse, String> transfer) {
      return transfer.getData()
          .map(result->ResponseEntity.ok().body(result))
          .orElse(ResponseEntity.internalServerError().body(new TransferResponse(EMPTY_STRING)));
    }

    private ResponseEntity<?> transferFail(Result<TransferResponse, String> transfer) {
        return transfer.getError()
            .map(error->ResponseEntity.badRequest().body(error))
            .orElse(ResponseEntity.badRequest().body(UNKNOWN_ERROR));
    }
}