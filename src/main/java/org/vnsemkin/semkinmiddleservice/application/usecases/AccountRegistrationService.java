package org.vnsemkin.semkinmiddleservice.application.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.AccountInfoResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.AccountRegistrationReq;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendErrorResponse;
import org.vnsemkin.semkinmiddleservice.application.external.BackendClientInterface;
import org.vnsemkin.semkinmiddleservice.application.mappers.AppMapper;
import org.vnsemkin.semkinmiddleservice.application.repositories.CustomerRepository;
import org.vnsemkin.semkinmiddleservice.domain.models.Account;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.AccountEntity;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public final class AccountRegistrationService {
    private final static String UNKNOWN_ERROR = "Неизвестная ошибка";
    private final static String DEFAULT_ACCOUNT_NAME = "Дебетовый";
    private final static String USER_NOT_EXIST = "Пользователь не зарегистрирован.";
    private final static String ACCOUNT_ALREADY_EXIST = "У Вас уже открыть счет.";
    private final static BigDecimal REGISTRATION_ACCOUNT_BONUS = new BigDecimal("5000.00");
    private final CustomerRepository customerRepository;
    private final BackendClientInterface backendClientInterface;
    private final AppMapper mapper = AppMapper.INSTANCE;

    public Result<Account, String> register(long tgId) {
        return customerRepository.findByTgId(tgId)
            .map(customer -> {
                if (customer.getAccount() != null) {
                    return Result.<Account, String>error(ACCOUNT_ALREADY_EXIST);
                }
                Result<String, BackendErrorResponse> registerResult =
                    registerAccountOnBackEnd(new AccountRegistrationReq(tgId, DEFAULT_ACCOUNT_NAME));
                return registerResult.isSuccess() ? getAccountInfo(customer) : registrationFail(registerResult);
            })
            .orElseGet(() -> Result.error(USER_NOT_EXIST));
    }

    private Result<String, BackendErrorResponse> registerAccountOnBackEnd(AccountRegistrationReq req) {
        return backendClientInterface.registerAccount(req);
    }

    private Result<Account, String> getAccountInfo(CustomerEntity customer) {
        Result<AccountInfoResponse, BackendErrorResponse> result = backendClientInterface.getAccount(customer.getTgId());
        return result.getData()
            .map(account -> {
                AccountEntity accountEntity = mapper.toAccountEntity(account);
                accountEntity.setBalance(addBonusToBalance(accountEntity.getBalance()));
                customer.setAccount(accountEntity);
                customerRepository.save(customer);
                return accountEntity;
            })
            .map(accountEntity -> Result.<Account, String>success(mapper.toAccount(accountEntity)))
            .orElseGet(() -> Result.error(
                result
                    .getError()
                    .map(BackendErrorResponse::message)
                    .orElse(UNKNOWN_ERROR)
            ));
    }

    private Result<Account, String> registrationFail(Result<String, BackendErrorResponse> register) {
        return register.getError()
            .map(error -> Result.<Account, String>error(error.message()))
            .orElse(Result.error(UNKNOWN_ERROR));
    }

    private BigDecimal addBonusToBalance(BigDecimal balance){
        return balance.add(REGISTRATION_ACCOUNT_BONUS);
    }
}