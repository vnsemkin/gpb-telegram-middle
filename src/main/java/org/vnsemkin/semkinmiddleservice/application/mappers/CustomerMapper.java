package org.vnsemkin.semkinmiddleservice.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.FrontReqDto;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.FrontRespDto;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    FrontRespDto toDto(Customer customer);
    FrontRespDto toDto(CustomerEntity customerEntity);
    CustomerEntity toEntity(Customer customer);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "passwordHash", source = "passwordHash")
    CustomerEntity toEntity(FrontReqDto frontReqDto, String passwordHash);
    Customer toCustomer(CustomerEntity customer);
}