package org.vnsemkin.semkinmiddleservice.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkinmiddleservice.application.mappers.AppMapper;
import org.vnsemkin.semkinmiddleservice.application.repositories.CustomerRepository;
import org.vnsemkin.semkinmiddleservice.application.usecases.AccountRegistrationService;
import org.vnsemkin.semkinmiddleservice.domain.models.Account;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;

@Service
@RequiredArgsConstructor
public final class AccountService {
    private final static String ACCOUNT_NOT_FOUND = "Счет не найден";
    private final AccountRegistrationService accountRegistrationService;
    private final CustomerRepository customerRepository;
    private final AppMapper mapper = AppMapper.INSTANCE;

    public Result<Account, String> createAccount(long tgId) {
        return accountRegistrationService.register(tgId);
    }

    public Result<Account, String> getAccount(long tgId) {
        return customerRepository.findByTgId(tgId)
            .map(customerEntity -> {
                Customer customer = mapper.toCustomer(customerEntity);
                return customer.account() == null ? Result.<Account, String>error(ACCOUNT_NOT_FOUND) :
                    Result.<Account, String>success(customer.account());
            })
            .orElse(Result.error(ACCOUNT_NOT_FOUND));
    }
}