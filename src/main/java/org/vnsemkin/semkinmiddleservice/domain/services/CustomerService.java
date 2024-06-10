package org.vnsemkin.semkinmiddleservice.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerInfoResponse;
import org.vnsemkin.semkinmiddleservice.application.mappers.AppMapper;
import org.vnsemkin.semkinmiddleservice.application.repositories.CustomerRepository;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final static String CUSTOMER_NOT_FOUND = "Пользователь не найден";
    private final CustomerRepository customerRepository;
    private final AppMapper mapper = AppMapper.INSTANCE;

    public Result<CustomerInfoResponse, String> getCustomerInfo(long id) {
        return customerRepository.findByTgId(id)
            .map(customerEntity ->
                Result.<CustomerInfoResponse, String>success(mapper.toCustomerInfoResponse(customerEntity)))
            .orElse(Result.error(CUSTOMER_NOT_FOUND));
    }
}
