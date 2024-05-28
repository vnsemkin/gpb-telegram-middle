package org.vnsemkin.semkinmiddleservice.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.vnsemkin.semkinmiddleservice.application.dtos.CustomerDto;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDto toDto(Customer customer);
    Customer toCustomer(CustomerDto customerDto);
    CustomerEntity toEntity(Customer customer);
    Customer toCustomer(CustomerEntity customer);
}
