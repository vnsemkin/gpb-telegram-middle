package org.vnsemkin.semkinmiddleservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendErrorResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerReqDto;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.ResultBackendDto;
import org.vnsemkin.semkinmiddleservice.application.external.BackendClientInterface;
import org.vnsemkin.semkinmiddleservice.application.mappers.CustomerMapper;
import org.vnsemkin.semkinmiddleservice.application.repositories.CustomerRepository;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.domain.services.CustomerRegistrationService;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;
import org.vnsemkin.semkinmiddleservice.presentation.exception.CustomerAlreadyRegisterException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class CustomerRegistrationServiceTest {
    private final static String TEST = "Test";
    private final static String USER_CREATED = "User created.";
    private final static String NAME = "John";
    private final static long LOCAL_ID = 123456789L;
    private final static String EMAIL = "john@example.com";
    private final static String PASSWORD = "password";
    private final static String UUID = "123e4567-e89b-12d3-a456-426614174000";
    CustomerMapper mapper = CustomerMapper.INSTANCE;

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
        // Arrange
        CustomerReqDto customerReqDto = new CustomerReqDto(NAME,
            EMAIL,
            PASSWORD);
        CustomerEntity entity = mapper.toEntity(customerReqDto);
        entity.setId(LOCAL_ID);
        entity.setUuid(UUID);

        when(customerRepository.findByEmail(customerReqDto.email())).thenReturn(Optional.of(entity));

        // Act
        Result<Customer> result = customerRegistrationService.register(customerReqDto);

        // Assert
        assertTrue(result.isFailure());
        assertTrue(result.getError().isPresent());
        assertInstanceOf(CustomerAlreadyRegisterException.class, result.getError().get());
    }

    @Test
    public void whenCustomerDoesNotExist_RegistrationSuccessful() {
        // Arrange
        CustomerReqDto customerReqDto = new CustomerReqDto(NAME, EMAIL, PASSWORD);
        CustomerEntity entity = mapper.toEntity(customerReqDto);
        entity.setId(LOCAL_ID);

        when(customerRepository.findByEmail(customerReqDto.email())).thenReturn(Optional.empty());
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(entity);
        when(backendClientInterface.registerCustomerOnBackend(anyLong())).thenAnswer(ans -> ResultBackendDto.success(USER_CREATED));
        when(backendClientInterface.getCustomerUuid(entity.getId()))
            .thenAnswer(ans -> ResultBackendDto.success(UUID));

        // Act
        Result<Customer> result = customerRegistrationService.register(customerReqDto);

        // Assert
        assertTrue(result.isSuccess());
        assertEquals(NAME, result.getData().get().name());
        assertEquals(EMAIL, result.getData().get().email());
        assertEquals(PASSWORD, result.getData().get().password());
        assertEquals(UUID, result.getData().get().uuid());
    }

    @Test
    public void whenCustomerDoesNotExist_RegistrationFail() {
        // Arrange
        CustomerReqDto customerReqDto = new CustomerReqDto(NAME, EMAIL, PASSWORD);
        CustomerEntity entity = mapper.toEntity(customerReqDto);
        entity.setId(LOCAL_ID);
        BackendErrorResponse backendErrorResponse = new BackendErrorResponse(TEST, TEST, TEST, TEST);


        when(customerRepository.findByEmail(customerReqDto.email())).thenReturn(Optional.empty());
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(entity);
        when(backendClientInterface.registerCustomerOnBackend(anyLong()))
            .thenAnswer(ans -> ResultBackendDto.failure(backendErrorResponse));

        // Act
        Result<Customer> result = customerRegistrationService.register(customerReqDto);
        // Assert
        assertTrue(result.isFailure());
        assertEquals(TEST, result.getError().get().getMessage());
    }

    @Test
    public void whenCustomerDoesNotExist_GetUuidFail() {
        // Arrange
        CustomerReqDto customerReqDto = new CustomerReqDto(NAME, EMAIL, PASSWORD);
        CustomerEntity entity = mapper.toEntity(customerReqDto);
        entity.setId(LOCAL_ID);
        BackendErrorResponse backendErrorResponse = new BackendErrorResponse(TEST, TEST, TEST, TEST);


        when(customerRepository.findByEmail(customerReqDto.email())).thenReturn(Optional.empty());
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(entity);
        when(backendClientInterface.registerCustomerOnBackend(anyLong()))
            .thenAnswer(ans -> ResultBackendDto.success(USER_CREATED));
        when(backendClientInterface.getCustomerUuid(entity.getId()))
            .thenAnswer(ans -> ResultBackendDto.failure(backendErrorResponse));

        // Act
        Result<Customer> result = customerRegistrationService.register(customerReqDto);
        // Assert
        assertTrue(result.isFailure());
        assertEquals(TEST, result.getError().get().getMessage());
    }
}

