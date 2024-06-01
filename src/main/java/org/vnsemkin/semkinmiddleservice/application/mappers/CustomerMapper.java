package org.vnsemkin.semkinmiddleservice.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerReqDto;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerRespDto;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerRespDto toDto(Customer customer);
    CustomerRespDto toDto(CustomerEntity customerEntity);
    CustomerEntity toEntity(Customer customer);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    CustomerEntity toEntity(CustomerReqDto customerReqDto);
    Customer toCustomer(CustomerEntity customer);
}
