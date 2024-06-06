package org.vnsemkin.semkinmiddleservice.mappers;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.FrontReqDto;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.FrontRespDto;
import org.vnsemkin.semkinmiddleservice.application.mappers.CustomerMapper;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CustomerMapperTest {
    private final static long TG_USER_ID = 137264783L;
    private final static String TG_USERNAME = "Test";
    private final static String FIRST_NAME = "John";
    private final static long LOCAL_ID = 123456789L;
    private final static String EMAIL = "john@example.com";
    private final static String PASSWORD = "password";
    private final static String UUID = "123e4567-e89b-12d3-a456-426614174000";

    private final CustomerMapper mapper = Mappers.getMapper(CustomerMapper.class);

    @Test
    public void testToDto_FromCustomer() {
        // ARRANGE
        Customer customer = new Customer(LOCAL_ID,
            TG_USER_ID, FIRST_NAME, TG_USERNAME, EMAIL, PASSWORD, UUID);

        // ACT
        FrontRespDto dto = mapper.toDto(customer);

        // ASSERT
        assertNotNull(dto);
        assertEquals(dto.firstName(), FIRST_NAME);
        assertEquals(dto.email(), EMAIL);
    }

    @Test
    public void testToDto_FromCustomerEntity() {
        // ARRANGE
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(LOCAL_ID);
        customerEntity.setFirstName(FIRST_NAME);
        customerEntity.setUsername(TG_USERNAME);
        customerEntity.setEmail(EMAIL);
        customerEntity.setPasswordHash(PASSWORD);
        // ACT
        FrontRespDto dto = mapper.toDto(customerEntity);

        // ASSERT
        assertNotNull(dto);
        assertEquals(dto.firstName(), FIRST_NAME);
        assertEquals(dto.email(), EMAIL);
    }

    @Test
    public void testToEntity_FromCustomer() {
        // ARRANGE
        Customer customer = new Customer(LOCAL_ID,
            TG_USER_ID, FIRST_NAME, TG_USERNAME, EMAIL, PASSWORD, UUID);

        // ACT
        CustomerEntity entity = mapper.toEntity(customer);

        // ASSERT
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
    public void testToEntity_FromCustomerReqDto() {
        // ARRANGE
        FrontReqDto dto = new FrontReqDto(TG_USER_ID,
            FIRST_NAME, TG_USERNAME, EMAIL, PASSWORD);

        // ACT
        CustomerEntity entity = mapper.toEntity(dto, PASSWORD);

        // ASSERT
        assertEquals(entity.getTgId(), TG_USER_ID);
        assertEquals(entity.getFirstName(), FIRST_NAME);
        assertEquals(entity.getUsername(), TG_USERNAME);
        assertEquals(entity.getEmail(), EMAIL);
        assertEquals(entity.getPasswordHash(), PASSWORD);
        assertNull(entity.getUuid());
    }

    @Test
    public void testToCustomer_FromCustomerEntity() {
        // ARRANGE
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(LOCAL_ID);
        customerEntity.setTgId(TG_USER_ID);
        customerEntity.setFirstName(FIRST_NAME);
        customerEntity.setUsername(TG_USERNAME);
        customerEntity.setEmail(EMAIL);
        customerEntity.setPasswordHash(PASSWORD);
        customerEntity.setUuid(UUID);

        // ACT
        Customer customer = mapper.toCustomer(customerEntity);

        // ASSERT
        assertEquals(customer.id(), LOCAL_ID);
        assertEquals(customer.tgId(), TG_USER_ID);
        assertEquals(customer.firstName(), FIRST_NAME);
        assertEquals(customer.username(), TG_USERNAME);
        assertEquals(customer.email(), EMAIL);
        assertEquals(customer.passwordHash(), PASSWORD);
        assertEquals(customer.uuid(), UUID);
    }
}