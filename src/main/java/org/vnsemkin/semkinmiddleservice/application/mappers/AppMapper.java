package org.vnsemkin.semkinmiddleservice.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.AccountInfoResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.AccountRegistrationResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerInfoResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerRegistrationRequest;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerRegistrationResponse;
import org.vnsemkin.semkinmiddleservice.domain.models.Account;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.AccountEntity;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.TransactionEntity;

import java.math.BigDecimal;

@Mapper
public interface AppMapper {
    AppMapper INSTANCE = Mappers.getMapper(AppMapper.class);

    CustomerRegistrationResponse toCustomerRegistrationResponse(Customer customer);
    CustomerRegistrationResponse toCustomerRegistrationResponse(CustomerEntity customerEntity);

    @Mapping(target = "account", source = "account")
    CustomerEntity toCustomerEntity(Customer customer);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "passwordHash", source = "passwordHash")
    @Mapping(target = "account", ignore = true)
    CustomerEntity toCustomerEntity(CustomerRegistrationRequest customerRegistrationRequest, String passwordHash);
    @Mapping(target = "account", source = "account")
    Customer toCustomer(CustomerEntity customer);
    AccountRegistrationResponse toAccountRegistrationResponse(Account account);
    @Mapping(target = "info", source = "info")
    AccountRegistrationResponse toAccountRegistrationResponse(Account account, String info);
    @Mapping(target = "customer", ignore = true)
    AccountEntity toAccountEntity(Account account);
    @Mapping(source = "accountId", target = "accountId")
    AccountEntity toAccountEntity(AccountInfoResponse accountInfoResponse);
    Account toAccount(AccountEntity accountEntity);
    @Mapping(target = "accountName",  source = "account.accountName")
    CustomerInfoResponse toCustomerInfoResponse(CustomerEntity customerEntity);
    @Mapping(target = "accountId", source = "customerEntity.account.id")
    @Mapping(target = "customerId", source = "customerEntity.id")
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "newBalance", source = "customerEntity.account.balance")
    @Mapping(target = "transactionUuid", source = "uuid")
    TransactionEntity toTransactionEntity(CustomerEntity customerEntity, BigDecimal amount, String uuid);
}