package org.vnsemkin.semkinmiddleservice.domain.services;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerReqDto;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.ResultBackendDto;
import org.vnsemkin.semkinmiddleservice.application.external.BackendClientInterface;
import org.vnsemkin.semkinmiddleservice.application.mappers.CustomerMapper;
import org.vnsemkin.semkinmiddleservice.application.repositories.CustomerRepository;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;
import org.vnsemkin.semkinmiddleservice.presentation.exception.CustomerAlreadyRegisterException;

import java.util.Optional;

@Service
public class CustomerRegistrationService {
    private final static String CUSTOMER_ALREADY_REGISTER = "Пользователь c email: %s уже зарегистрирован.";
    private final static String INVALID_UUID = "Invalid UUID";
    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper = CustomerMapper.INSTANCE;
    private final BackendClientInterface backendClientInterface;

    public CustomerRegistrationService(CustomerRepository customerRepository,
                                       BackendClientInterface backendClientInterface) {
        this.customerRepository = customerRepository;
        this.backendClientInterface = backendClientInterface;
    }

    public Result<Customer> register(@NonNull CustomerReqDto customerReqDto) {
        Optional<CustomerEntity> customerEntityByEmail = customerRepository
            .findByEmail(customerReqDto.email());
        if (customerEntityByEmail.isPresent()) {
            Result<Customer> customerResult =
                checkIfCustomerHasUuidElseRegisterOnBackend(customerEntityByEmail.get());
            return ifFailureRemoveCustomerFromDb(customerResult, customerEntityByEmail.get());
        } else {
            return registerCustomerOnBackend(saveCustomerAndGetId(customerReqDto));
        }
    }

    private Result<Customer> ifFailureRemoveCustomerFromDb(@NonNull Result<Customer> customerResult,
                                                           CustomerEntity customerEntityByEmail) {
        if (customerResult.isFailure()) {
            customerRepository.delete(customerEntityByEmail);
            return customerResult;
        }
        return customerResult;
    }

    private Result<Customer> checkIfCustomerHasUuidElseRegisterOnBackend(@NonNull CustomerEntity customerEntity) {
        if (customerEntity.getUuid() != null) {
            return Result.failure(new CustomerAlreadyRegisterException(String
                .format(CUSTOMER_ALREADY_REGISTER, customerEntity.getEmail())));
        } else {
            return registerCustomerOnBackend(customerEntity);
        }
    }

    private CustomerEntity saveCustomerAndGetId(@NonNull CustomerReqDto customerReqDto) {
        return customerRepository.save(mapper.toEntity(customerReqDto));
    }

    private Result<Customer> registerCustomerOnBackend(@NonNull CustomerEntity customerEntity) {
        ResultBackendDto<?> registerResult = backendClientInterface
            .registerCustomerOnBackend(customerEntity.getId());
        if (registerResult.isSuccess()) {
            return getSavedOnBackendCustomerUuid(customerEntity);
        }
        return Result.failure(new RuntimeException(registerResult.getError().get().message()));
    }

    private Result<Customer> getSavedOnBackendCustomerUuid(@NonNull CustomerEntity customerEntity) {
        ResultBackendDto<?> customerWithUuid = backendClientInterface
            .getCustomerUuid(customerEntity.getId());
        if (customerWithUuid.isSuccess()) {
            return setUuidToCustomer(customerEntity, customerWithUuid);
        } else {
            return Result.failure(new RuntimeException(customerWithUuid.getError().get().message()));
        }
    }

    private Result<Customer> setUuidToCustomer(@NonNull CustomerEntity customerEntity,
                                               @NonNull ResultBackendDto<?> uuid) {
        if (uuid.getData().get() instanceof String) {
            customerEntity.setUuid((String) uuid.getData().get());
            return Result.success(mapper.toCustomer(customerRepository.save(customerEntity)));
        } else {
            return Result.failure(new RuntimeException(INVALID_UUID));
        }
    }
}