package org.vnsemkin.semkinmiddleservice.mappers;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerReqDto;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerRespDto;
import org.vnsemkin.semkinmiddleservice.application.mappers.CustomerMapper;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CustomerMapperTest {

    private final CustomerMapper mapper = Mappers.getMapper(CustomerMapper.class);

    @Test
    public void testToDto_FromCustomer() {
        // Arrange
        long id = 19842141241L;
        String name = "John";
        String email = "john.doe@example.com";
        String password = "password";
        String uuid = "5f59e024-03c7-498d-9fc9-b8b15fd49c47";
        Customer customer = new Customer(id, name, email, password, uuid);


        // Act
        CustomerRespDto dto = mapper.toDto(customer);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.name()).isEqualTo("John");
        assertThat(dto.email()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void testToDto_FromCustomerEntity() {
        // Arrange
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setName("John");
        customerEntity.setEmail("john.doe@example.com");

        // Act
        CustomerRespDto dto = mapper.toDto(customerEntity);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.name()).isEqualTo("John");
        assertThat(dto.email()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void testToEntity_FromCustomer() {
        // Arrange
        long id = 19842141241L;
        String name = "John";
        String email = "john.doe@example.com";
        String password = "password";
        String uuid = "5f59e024-03c7-498d-9fc9-b8b15fd49c47";
        Customer customer = new Customer(id, name, email, password, uuid);

        // Act
        CustomerEntity entity = mapper.toEntity(customer);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("John");
        assertThat(entity.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(entity.getPassword()).isEqualTo("password");
        assertThat(entity.getUuid()).isEqualTo("5f59e024-03c7-498d-9fc9-b8b15fd49c47");
    }

    @Test
    public void testToEntity_FromCustomerReqDto() {
        // Arrange
        CustomerReqDto dto = new CustomerReqDto("John",
            "john.doe@example.com",
            "password");

        // Act
        CustomerEntity entity = mapper.toEntity(dto);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("John");
        assertThat(entity.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void testToCustomer_FromCustomerEntity() {
        // Arrange
        CustomerEntity entity = new CustomerEntity();
        entity.setName("John");
        entity.setEmail("john.doe@example.com");
        entity.setPassword("password");
        entity.setUuid("5f59e024-03c7-498d-9fc9-b8b15fd49c47");

        // Act
        Customer customer = mapper.toCustomer(entity);

        // Assert
        assertThat(customer).isNotNull();
        assertThat(customer.name()).isEqualTo("John");
        assertThat(customer.email()).isEqualTo("john.doe@example.com");
    }
}