package org.vnsemkin.semkinmiddleservice.application.mappers;

import javax.annotation.processing.Generated;
import org.vnsemkin.semkinmiddleservice.application.dtos.CustomerDto;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-28T11:10:07+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public CustomerDto toDto(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        String name = null;
        String email = null;

        name = customer.name();
        email = customer.email();

        CustomerDto customerDto = new CustomerDto( name, email );

        return customerDto;
    }

    @Override
    public Customer toCustomer(CustomerDto customerDto) {
        if ( customerDto == null ) {
            return null;
        }

        String name = null;
        String email = null;

        name = customerDto.name();
        email = customerDto.email();

        String password = null;

        Customer customer = new Customer( name, email, password );

        return customer;
    }

    @Override
    public CustomerEntity toEntity(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setName( customer.name() );
        customerEntity.setEmail( customer.email() );
        customerEntity.setPassword( customer.password() );

        return customerEntity;
    }

    @Override
    public Customer toCustomer(CustomerEntity customer) {
        if ( customer == null ) {
            return null;
        }

        String name = null;
        String email = null;
        String password = null;

        name = customer.getName();
        email = customer.getEmail();
        password = customer.getPassword();

        Customer customer1 = new Customer( name, email, password );

        return customer1;
    }
}
