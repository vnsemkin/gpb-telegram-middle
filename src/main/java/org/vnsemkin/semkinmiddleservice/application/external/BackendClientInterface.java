package org.vnsemkin.semkinmiddleservice.application.external;

import org.vnsemkin.semkinmiddleservice.application.dtos.back.AccountRegistrationReq;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendErrorResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRegistrationReq;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRespUuid;
import org.vnsemkin.semkinmiddleservice.domain.models.Account;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;

public interface BackendClientInterface {
    Result<String, BackendErrorResponse> registerCustomer(BackendRegistrationReq req);
    Result<BackendRespUuid, BackendErrorResponse> getCustomerUuid(BackendRegistrationReq req);
    Result<String, BackendErrorResponse> registerAccount(AccountRegistrationReq req);
    Result<Account, BackendErrorResponse> getAccount(long tgId);
}