package org.vnsemkin.semkinmiddleservice.application.usecases;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendErrorResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.TransferRequest;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.TransferResponse;
import org.vnsemkin.semkinmiddleservice.application.external.BackendClientInterface;
import org.vnsemkin.semkinmiddleservice.application.mappers.AppMapper;
import org.vnsemkin.semkinmiddleservice.application.repositories.CustomerRepository;
import org.vnsemkin.semkinmiddleservice.application.repositories.TransactionRepository;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferMoneyService {
    private static final String NOT_ENOUGH_MONEY = "Недостаточно денег не счету.";
    private static final String UNKNOWN_ERROR = "Неизвестная ошибка ";
    private static final String CUSTOMER_NOT_FOUND = "Вы не зарегистрированы в сервисе";
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final BackendClientInterface backendClientInterface;
    private final AppMapper mapper = AppMapper.INSTANCE;


    @Transactional
    public Result<TransferResponse, String> transferMoney(TransferRequest transferRequest) {
        BigDecimal amount = new BigDecimal(transferRequest.amount());
        String fromUsername = transferRequest.from();
        return customerRepository.findByUsername(fromUsername)
            .map(customer -> {
                if (isBalanceLow(customer, amount)) {
                    return Result.<TransferResponse, String>error(NOT_ENOUGH_MONEY);
                } else {
                    return requestTransferMoney(transferRequest, customer, amount);
                }
            })
            .orElseGet(() -> Result.error(CUSTOMER_NOT_FOUND));
    }

    private boolean isBalanceLow(CustomerEntity customer, BigDecimal amount) {
        return customer.getAccount().getBalance().compareTo(amount) < 0;
    }

    private Result<TransferResponse, String> requestTransferMoney(TransferRequest transferRequest,
                                                                  CustomerEntity customerEntity,
                                                                  BigDecimal amount) {
        Result<TransferResponse, BackendErrorResponse> result =
            backendClientInterface.transferMoney(transferRequest);
        return result.getData()
            .map(response -> {
                handleTransaction(response.transferId(), customerEntity, amount);
                return Result.<TransferResponse, String>success(response);
            })
            .orElse(result.getError()
                .map(err -> Result.<TransferResponse, String>error(err.message()))
                .orElse(Result.error(UNKNOWN_ERROR)));
    }

    private void handleTransaction(String uuid,
                                   CustomerEntity customerEntity,
                                   BigDecimal amount) {
        BigDecimal balance = customerEntity.getAccount().getBalance().subtract(amount);
        customerEntity.getAccount().setBalance(balance);
        customerRepository.save(customerEntity);
        transactionRepository.save(mapper.toTransactionEntity(customerEntity, amount, uuid));
    }
}