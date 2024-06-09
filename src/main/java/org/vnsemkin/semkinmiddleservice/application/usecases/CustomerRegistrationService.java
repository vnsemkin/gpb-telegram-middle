package org.vnsemkin.semkinmiddleservice.application.usecases;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendErrorResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRegistrationReq;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRespUuid;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerRegistrationRequest;
import org.vnsemkin.semkinmiddleservice.application.external.BackendClientInterface;
import org.vnsemkin.semkinmiddleservice.application.mappers.AppMapper;
import org.vnsemkin.semkinmiddleservice.application.repositories.CustomerRepository;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.domain.services.PasswordService;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;

import java.util.Optional;

@Service
public class CustomerRegistrationService {
    private final static String CUSTOMER_ALREADY_REGISTER = "Пользователь уже зарегистрирован.";
    private final static String UNKNOWN_ERROR = "Неизвестная ошибка";
    private final CustomerRepository customerRepository;
    private final AppMapper mapper = AppMapper.INSTANCE;
    private final BackendClientInterface backendClientInterface;
    private final PasswordService passwordService;

    public CustomerRegistrationService(CustomerRepository customerRepository,
                                       BackendClientInterface backendClientInterface, PasswordService passwordService) {
        this.customerRepository = customerRepository;
        this.backendClientInterface = backendClientInterface;
        this.passwordService = passwordService;
    }

    public Result<Customer, String> register(@NonNull CustomerRegistrationRequest customerRegistrationRequest) {
        Optional<CustomerEntity> customerEntity = customerRepository
            .findByTgId(customerRegistrationRequest.tgId());
        return customerEntity.map(this::customerExistInDb)
            .orElseGet(() -> customerNotExistInDb(customerRegistrationRequest));
    }

    private Result<Customer, String> customerExistInDb(@NonNull CustomerEntity customerEntity) {
        Result<Customer, String> result = ifHasUuidThenErrorElseRegisterOnBackend(customerEntity);
        return result.isSuccess() ? result :
        handleCustomerRegistrationError(result, customerEntity);
    }

    private Result<Customer, String> customerNotExistInDb(CustomerRegistrationRequest customerRegistrationRequest) {
        CustomerEntity customerEntity = saveCustomerInDb(customerRegistrationRequest);
        Result<Customer, String> customerStringResult = registerCustomerOnBackend(customerEntity);
        return handleCustomerRegistrationError(customerStringResult, customerEntity);

    }

    private Result<Customer, String> ifHasUuidThenErrorElseRegisterOnBackend(@NonNull CustomerEntity customerEntity) {
        return customerEntity.getUuid() != null ?
            Result.error(CUSTOMER_ALREADY_REGISTER) :
            registerCustomerOnBackend(customerEntity);
    }

    private Result<Customer, String> registerCustomerOnBackend(@NonNull CustomerEntity customerEntity) {
        Result<String, BackendErrorResponse> registerResult = backendClientInterface
            .registerCustomer(new BackendRegistrationReq(customerEntity.getTgId()));
        if (registerResult.isSuccess()) {
            return getSavedOnBackendCustomerUuid(customerEntity);
        }
        return registerResult.getError()
            .map(error -> Result.<Customer, String>error(error.message()))
            .orElse(Result.error(UNKNOWN_ERROR));
    }

    private Result<Customer, String> getSavedOnBackendCustomerUuid(@NonNull CustomerEntity customerEntity) {
        Result<BackendRespUuid, BackendErrorResponse> customerWithUuid = backendClientInterface
            .getCustomerUuid(new BackendRegistrationReq(customerEntity.getTgId()));
        if (customerWithUuid.isSuccess()) {
            return customerWithUuid.getData()
                .map(data -> {
                    customerEntity.setUuid(data.uuid());
                    customerRepository.save(customerEntity);
                    return Result.<Customer, String>success(mapper.toCustomer(customerEntity));
                })
                .orElseGet(() -> Result.error(UNKNOWN_ERROR));
        }
        return customerWithUuid.getError()
            .map(error -> Result.<Customer, String>error(error.message()))
            .orElse(Result.error(UNKNOWN_ERROR));
    }

    private CustomerEntity saveCustomerInDb(@NonNull CustomerRegistrationRequest customerRegistrationRequest) {
        String encodePassword = passwordService.hashPassword(customerRegistrationRequest.password());
        return customerRepository.save(mapper.toCustomerEntity(customerRegistrationRequest, encodePassword));
    }

    private Result<Customer, String> handleCustomerRegistrationError(@NonNull Result<Customer,
        String> customerResult, CustomerEntity customerEntity) {
        customerResult.getError()
            .filter(error -> !error.equals(CUSTOMER_ALREADY_REGISTER))
            .ifPresent(error -> customerRepository.delete(customerEntity));
        return customerResult;
    }
}