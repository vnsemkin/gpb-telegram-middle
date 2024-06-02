package org.vnsemkin.semkinmiddleservice.application.usecases;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendErrorResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRegistrationReq;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRespUuid;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.FrontReqDto;
import org.vnsemkin.semkinmiddleservice.application.external.BackendClientInterface;
import org.vnsemkin.semkinmiddleservice.application.mappers.CustomerMapper;
import org.vnsemkin.semkinmiddleservice.application.repositories.CustomerRepository;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.domain.services.PasswordService;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;

import java.util.Optional;

@Service
public class CustomerRegistrationService {
    private final static String CUSTOMER_ALREADY_REGISTER = "Пользователь c email: %s уже зарегистрирован.";
    private final static String UNKNOWN_ERROR = "Неизвестная ошибка";
    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper = CustomerMapper.INSTANCE;
    private final BackendClientInterface backendClientInterface;
    private final PasswordService passwordService;

    public CustomerRegistrationService(CustomerRepository customerRepository,
                                       BackendClientInterface backendClientInterface, PasswordService passwordService) {
        this.customerRepository = customerRepository;
        this.backendClientInterface = backendClientInterface;
        this.passwordService = passwordService;
    }

    public Result<Customer, String> register(@NonNull FrontReqDto frontReqDto) {
        Optional<CustomerEntity> customerEntityByEmail = customerRepository
            .findByEmail(frontReqDto.email());
        if (customerEntityByEmail.isPresent()) {
            Result<Customer, String> customerResult =
                checkIfCustomerHasUuidElseRegisterOnBackend(customerEntityByEmail.get());
            return ifRegistrationHasErrorRemoveCustomerFromDb(customerResult, customerEntityByEmail.get());
        } else {
            CustomerEntity customerEntity = saveCustomerAndGetId(frontReqDto);
            Result<Customer, String> customerResult = registerCustomerOnBackend(customerEntity);
            return ifRegistrationHasErrorRemoveCustomerFromDb(customerResult, customerEntity);
        }
    }

    private Result<Customer, String> checkIfCustomerHasUuidElseRegisterOnBackend(@NonNull CustomerEntity customerEntity) {
        if (customerEntity.getUuid() != null) {
            return Result.error(String.format(CUSTOMER_ALREADY_REGISTER,
                customerEntity.getEmail()));
        } else {
            return registerCustomerOnBackend(customerEntity);
        }
    }

    private Result<Customer, String> registerCustomerOnBackend(@NonNull CustomerEntity customerEntity) {
        Result<String, BackendErrorResponse> registerResult = backendClientInterface
            .registerCustomerOnBackend(new BackendRegistrationReq(customerEntity.getId()));
        if (registerResult.isSuccess()) {
            return getSavedOnBackendCustomerUuid(customerEntity);
        }
        return registerResult.getError()
            .map(error -> Result.<Customer, String>error(error.message()))
            .orElse(Result.error(UNKNOWN_ERROR));
    }

    private Result<Customer, String> getSavedOnBackendCustomerUuid(@NonNull CustomerEntity customerEntity) {
        Result<BackendRespUuid, BackendErrorResponse> customerWithUuid = backendClientInterface
            .getCustomerUuid(new BackendRegistrationReq(customerEntity.getId()));
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

    private CustomerEntity saveCustomerAndGetId(@NonNull FrontReqDto frontReqDto) {
        String encodePassword = passwordService.hashPassword(frontReqDto.password());
        return customerRepository.save(mapper.toEntity(frontReqDto, encodePassword));
    }

    private Result<Customer, String> ifRegistrationHasErrorRemoveCustomerFromDb(@NonNull Result<Customer,
        String> customerResult, CustomerEntity customerEntity) {
        if (customerResult.isError()) {
            customerRepository.delete(customerEntity);
            return customerResult;
        }
        return customerResult;
    }
}