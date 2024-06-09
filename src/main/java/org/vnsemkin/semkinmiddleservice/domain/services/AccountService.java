package org.vnsemkin.semkinmiddleservice.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkinmiddleservice.application.usecases.AccountRegistrationService;
import org.vnsemkin.semkinmiddleservice.domain.models.Account;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;

@Service
@RequiredArgsConstructor
public final class AccountService {
    private final AccountRegistrationService accountRegistrationService;

    public Result<Account, String> createAccount(long tgId) {
        return accountRegistrationService.register(tgId);
    }
}