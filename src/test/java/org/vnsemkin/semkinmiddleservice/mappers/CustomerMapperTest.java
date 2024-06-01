package org.vnsemkin.semkinmiddleservice.mappers;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.FrontReqDto;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.FrontRespDto;
import org.vnsemkin.semkinmiddleservice.application.mappers.CustomerMapper;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CustomerMapperTest {
    private final static String NAME = "John";
    private final static long LOCAL_ID = 123456789L;
    private final static String EMAIL = "john@example.com";
    private final static String PASSWORD = "password";
    private final static String UUID = "123e4567-e89b-12d3-a456-426614174000";

    private final CustomerMapper mapper = Mappers.getMapper(CustomerMapper.class);

    @Test
    public void testToDto_FromCustomer() {
        // ARRANGE
        Customer customer = new Customer(LOCAL_ID, NAME, EMAIL, PASSWORD, UUID);
        
        // ACT
        FrontRespDto dto = mapper.toDto(customer);

        // ASSERT
        assertThat(dto).isNotNull();
        assertThat(dto.name()).isEqualTo(NAME);
        assertThat(dto.email()).isEqualTo(EMAIL);
    }

    @Test
    public void testToDto_FromCustomerEntity() {
        // ARRANGE
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setName(NAME);
        customerEntity.setEmail(EMAIL);

        // ACT
        FrontRespDto dto = mapper.toDto(customerEntity);

        // ASSERT
        assertThat(dto).isNotNull();
        assertThat(dto.name()).isEqualTo(NAME);
        assertThat(dto.email()).isEqualTo(EMAIL);
    }

    @Test
    public void testToEntity_FromCustomer() {
        // ARRANGE
        Customer customer = new Customer(LOCAL_ID, NAME, EMAIL, PASSWORD, UUID);

        // ACT
        CustomerEntity entity = mapper.toEntity(customer);

        // ASSERT
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo(NAME);
        assertThat(entity.getEmail()).isEqualTo(EMAIL);
        assertThat(entity.getPassword()).isEqualTo(PASSWORD);
        assertThat(entity.getUuid()).isEqualTo(UUID);
    }

    @Test
    public void testToEntity_FromCustomerReqDto() {
        // ARRANGE
        FrontReqDto dto = new FrontReqDto(NAME,
            EMAIL,
            PASSWORD);

        // ACT
        CustomerEntity entity = mapper.toEntity(dto);

        // ASSERT
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo(NAME);
        assertThat(entity.getEmail()).isEqualTo(EMAIL);
    }

    @Test
    public void testToCustomer_FromCustomerEntity() {
        // ARRANGE
        CustomerEntity entity = new CustomerEntity();
        entity.setName(NAME);
        entity.setEmail(EMAIL);
        entity.setPassword(PASSWORD);
        entity.setUuid(UUID);

        // ACT
        Customer customer = mapper.toCustomer(entity);

        // ASSERT
        assertThat(customer).isNotNull();
        assertThat(customer.name()).isEqualTo(NAME);
        assertThat(customer.email()).isEqualTo(EMAIL);
    }
}