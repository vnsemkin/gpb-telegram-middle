package org.vnsemkin.semkinmiddleservice.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkinmiddleservice.application.mappers.CustomerMapper;
import org.vnsemkin.semkinmiddleservice.application.repositories.CustomerRepository;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;
import org.vnsemkin.semkinmiddleservice.presentation.exception.CustomerAlreadyRegisterException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerRegistrationService {
    private final static String CUSTOMER_ALREADY_REGISTER = "Пользователь c email: %s уже зарегистрирован.";
    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper = CustomerMapper.INSTANCE;

    public Result<Customer> register(@NonNull Customer customer) {
        CustomerEntity customerEntity = mapper.toEntity(customer);
        Optional<CustomerEntity> byEmail = customerRepository
            .findByEmail(customerEntity.getEmail());
        if (byEmail.isPresent()) {
            return Result.failure(new CustomerAlreadyRegisterException(String
                .format(CUSTOMER_ALREADY_REGISTER, customerEntity.getEmail())));
        }
        return Result.success(mapper.toCustomer(customerRepository.save(customerEntity)));
    }
}

