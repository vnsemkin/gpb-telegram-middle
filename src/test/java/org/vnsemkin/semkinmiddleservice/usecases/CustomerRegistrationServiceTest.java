package org.vnsemkin.semkinmiddleservice.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendErrorResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRegistrationReq;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRespUuid;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerRegistrationRequest;
import org.vnsemkin.semkinmiddleservice.application.external.BackendClientInterface;
import org.vnsemkin.semkinmiddleservice.application.mappers.AppMapper;
import org.vnsemkin.semkinmiddleservice.application.repositories.CustomerRepository;
import org.vnsemkin.semkinmiddleservice.application.usecases.CustomerRegistrationService;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.domain.services.PasswordService;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CustomerRegistrationServiceTest {
    private final static long TG_USER_ID = 137264783L;
    private final static String TG_USERNAME = "Test";
    private final static String FIRST_NAME = "John";
    private final static String TEST = "Test";
    private final static String USER_CREATED = "User created.";
    private final static long LOCAL_ID = 123456789L;
    private final static String EMAIL = "john@example.com";
    private final static String PASSWORD = "password";
    private final static String PASSWORD_HASH = "password_hash";
    private final static String UUID = "123e4567-e89b-12d3-a456-426614174000";
    private final static String CUSTOMER_ALREADY_REGISTER = "Пользователь уже зарегистрирован.";
    AppMapper mapper = AppMapper.INSTANCE;
    BackendRegistrationReq req = new BackendRegistrationReq(LOCAL_ID);

    @Mock
    PasswordService passwordService;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    BackendClientInterface backendClientInterface;
    @InjectMocks
    CustomerRegistrationService customerRegistrationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenCustomerAlreadyExistsInDbWithUuid_ThrowException() {
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(TG_USER_ID, FIRST_NAME, TG_USERNAME, EMAIL, PASSWORD);
        CustomerEntity entity = mapper.toCustomerEntity(customerRegistrationRequest, PASSWORD);
        entity.setId(LOCAL_ID);
        entity.setUuid(UUID);

        when(customerRepository.findByTgId(customerRegistrationRequest.tgId())).thenReturn(Optional.of(entity));

        Result<Customer, String> result = customerRegistrationService.register(customerRegistrationRequest);

        assertTrue(result.isError());
        assertTrue(result.getError().isPresent());
        assertEquals(CUSTOMER_ALREADY_REGISTER, result.getError().get());
    }

    @Test
    public void whenCustomerDoesNotExist_RegistrationSuccessful() {
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(TG_USER_ID, FIRST_NAME, TG_USERNAME, EMAIL, PASSWORD);
        CustomerEntity entity = mapper.toCustomerEntity(customerRegistrationRequest, PASSWORD);
        entity.setTgId(LOCAL_ID);
        BackendRespUuid backendRespUuid = new BackendRespUuid(UUID);

        when(customerRepository.findByTgId(customerRegistrationRequest.tgId())).thenReturn(Optional.empty());
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(entity);
        when(backendClientInterface.registerCustomer(req)).thenAnswer(ans ->
            Result.success(USER_CREATED));
        when(backendClientInterface.getCustomerUuid(req))
            .thenAnswer(ans -> Result.success(backendRespUuid));
        when(passwordService.hashPassword(PASSWORD)).thenReturn(PASSWORD_HASH);

        Result<Customer, String> resultWithUuid = customerRegistrationService.register(customerRegistrationRequest);

        assertTrue(resultWithUuid.isSuccess());
        assertEquals(FIRST_NAME, resultWithUuid.getData().get().firstName());
        assertEquals(EMAIL, resultWithUuid.getData().get().email());
        assertEquals(PASSWORD, resultWithUuid.getData().get().passwordHash());
        assertEquals(UUID, resultWithUuid.getData().get().uuid());
    }

    @Test
    public void whenBackendReturnError_RegistrationFail() {
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(TG_USER_ID, FIRST_NAME, TG_USERNAME, EMAIL, PASSWORD);
        CustomerEntity entity = mapper.toCustomerEntity(customerRegistrationRequest, PASSWORD);
        entity.setTgId(LOCAL_ID);
        BackendErrorResponse backendErrorResponse = new BackendErrorResponse(TEST, TEST, TEST, TEST);


        when(customerRepository.findByTgId(customerRegistrationRequest.tgId())).thenReturn(Optional.of(entity));
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(entity);
        when(backendClientInterface.registerCustomer(req))
            .thenAnswer(ans -> Result.error(backendErrorResponse));

        Result<Customer, String> result = customerRegistrationService.register(customerRegistrationRequest);

        assertTrue(result.isError());
        assertEquals(TEST, result.getError().get());
    }

    @Test
    public void whenCustomerExist_ReturnUuid() {
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(TG_USER_ID, FIRST_NAME, TG_USERNAME, EMAIL, PASSWORD);
        CustomerEntity entity = mapper.toCustomerEntity(customerRegistrationRequest, PASSWORD);
        entity.setTgId(LOCAL_ID);
        BackendRespUuid backendRespUuid = new BackendRespUuid(UUID);

        when(customerRepository.findByTgId(customerRegistrationRequest.tgId())).thenReturn(Optional.of(entity));
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(entity);
        when(backendClientInterface.registerCustomer(req))
            .thenAnswer(ans -> Result.success(USER_CREATED));
        when(backendClientInterface.getCustomerUuid(req))
            .thenAnswer(ans -> Result.success(backendRespUuid));

        Result<Customer, String> result = customerRegistrationService.register(customerRegistrationRequest);

        assertTrue(result.isSuccess());
        assertEquals(UUID, result.getData().get().uuid());
    }

    @Test
    public void whenBackendReturnErrorOnUuidRequest() {
        CustomerRegistrationRequest customerRegistrationRequest =
            new CustomerRegistrationRequest(TG_USER_ID, FIRST_NAME, TG_USERNAME, EMAIL, PASSWORD);
        CustomerEntity entity = mapper.toCustomerEntity(customerRegistrationRequest, PASSWORD);
        entity.setTgId(LOCAL_ID);
        BackendErrorResponse backendErrorResponse = new BackendErrorResponse(TEST, TEST, TEST, TEST);

        when(customerRepository.findByTgId(customerRegistrationRequest.tgId())).thenReturn(Optional.of(entity));
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(entity);
        when(backendClientInterface.registerCustomer(req))
            .thenAnswer(ans -> Result.success(USER_CREATED));
        when(backendClientInterface.getCustomerUuid(req))
            .thenAnswer(ans -> Result.error(backendErrorResponse));

        Result<Customer, String> result = customerRegistrationService.register(customerRegistrationRequest);

        assertTrue(result.isError());
        assertEquals(TEST, result.getError().get());
    }
}