package org.vnsemkin.semkinmiddleservice.application.external;

import org.vnsemkin.semkinmiddleservice.application.dtos.back.AccountInfoResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.AccountRegistrationReq;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendErrorResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRegistrationReq;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRespUuid;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.TransferRequest;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.TransferResponse;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;

public interface BackendClientInterface {
    Result<String, BackendErrorResponse> registerCustomer(BackendRegistrationReq req);
    Result<BackendRespUuid, BackendErrorResponse> getCustomerUuid(String username);
    Result<BackendRespUuid, BackendErrorResponse> getCustomerUuid(long id);
    Result<String, BackendErrorResponse> registerAccount(AccountRegistrationReq req);
    Result<AccountInfoResponse, BackendErrorResponse> getAccount(long tgId);
    Result<TransferResponse, BackendErrorResponse> transferMoney(TransferRequest request);
}