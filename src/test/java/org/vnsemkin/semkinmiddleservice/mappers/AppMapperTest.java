package org.vnsemkin.semkinmiddleservice.mappers;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerRegistrationRequest;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerRegistrationResponse;
import org.vnsemkin.semkinmiddleservice.application.mappers.AppMapper;
import org.vnsemkin.semkinmiddleservice.domain.models.Account;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AppMapperTest {
    private final static long TG_USER_ID = 137264783L;
    private final static String TG_USERNAME = "Test";
    private final static String FIRST_NAME = "John";
    private final static long LOCAL_ID = 123456789L;
    private final static String EMAIL = "john@example.com";
    private final static String PASSWORD = "password";
    private final static String UUID = "123e4567-e89b-12d3-a456-426614174000";
    private final static String TEST_STRING = "Test";
    private final static BigDecimal BALANCE = new BigDecimal("100.00");

    private final AppMapper mapper = Mappers.getMapper(AppMapper.class);

    @Test
    public void testToCustomerRegistrationResponse_FromCustomer() {
        Account account = new Account(LOCAL_ID,UUID, TEST_STRING, BALANCE);
        Customer customer = new Customer(LOCAL_ID,
            TG_USER_ID, FIRST_NAME, TG_USERNAME, EMAIL, PASSWORD, UUID, account);

        CustomerRegistrationResponse dto = mapper.toCustomerRegistrationResponse(customer);

        assertNotNull(dto);
        assertEquals(dto.firstName(), FIRST_NAME);
        assertEquals(dto.email(), EMAIL);
    }

    @Test
    public void testToCustomerRegistrationResponse_FromCustomerEntity() {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(LOCAL_ID);
        customerEntity.setFirstName(FIRST_NAME);
        customerEntity.setUsername(TG_USERNAME);
        customerEntity.setEmail(EMAIL);
        customerEntity.setPasswordHash(PASSWORD);

        CustomerRegistrationResponse dto = mapper.toCustomerRegistrationResponse(customerEntity);

        assertNotNull(dto);
        assertEquals(dto.firstName(), FIRST_NAME);
        assertEquals(dto.email(), EMAIL);
    }

    @Test
    public void testToCustomerEntity_FromCustomer() {
        Account account = new Account(LOCAL_ID,UUID, TEST_STRING, BALANCE);
        Customer customer = new Customer(LOCAL_ID,
            TG_USER_ID, FIRST_NAME, TG_USERNAME, EMAIL, PASSWORD, UUID,account);

        CustomerEntity entity = mapper.toCustomerEntity(customer);

        assertNotNull(entity);
        assertEquals(entity.getId(), LOCAL_ID);
        assertEquals(entity.getTgId(), TG_USER_ID);
        assertEquals(entity.getFirstName(), FIRST_NAME);
        assertEquals(entity.getUsername(), TG_USERNAME);
        assertEquals(entity.getEmail(), EMAIL);
        assertEquals(entity.getPasswordHash(), PASSWORD);
        assertEquals(entity.getUuid(), UUID);
    }

    @Test
    public void testToCustomerEntity_FromCustomerReqDto() {
        CustomerRegistrationRequest dto = new CustomerRegistrationRequest(TG_USER_ID,
            FIRST_NAME, TG_USERNAME, EMAIL, PASSWORD);

        CustomerEntity entity = mapper.toCustomerEntity(dto, PASSWORD);

        assertEquals(entity.getTgId(), TG_USER_ID);
        assertEquals(entity.getFirstName(), FIRST_NAME);
        assertEquals(entity.getUsername(), TG_USERNAME);
        assertEquals(entity.getEmail(), EMAIL);
        assertEquals(entity.getPasswordHash(), PASSWORD);
        assertNull(entity.getUuid());
    }

    @Test
    public void testToCustomer_FromCustomerEntity() {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(LOCAL_ID);
        customerEntity.setTgId(TG_USER_ID);
        customerEntity.setFirstName(FIRST_NAME);
        customerEntity.setUsername(TG_USERNAME);
        customerEntity.setEmail(EMAIL);
        customerEntity.setPasswordHash(PASSWORD);
        customerEntity.setUuid(UUID);

        Customer customer = mapper.toCustomer(customerEntity);

        assertEquals(customer.id(), LOCAL_ID);
        assertEquals(customer.tgId(), TG_USER_ID);
        assertEquals(customer.firstName(), FIRST_NAME);
        assertEquals(customer.username(), TG_USERNAME);
        assertEquals(customer.email(), EMAIL);
        assertEquals(customer.passwordHash(), PASSWORD);
        assertEquals(customer.uuid(), UUID);
    }
}