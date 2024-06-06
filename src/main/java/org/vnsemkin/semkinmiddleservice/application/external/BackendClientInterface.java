package org.vnsemkin.semkinmiddleservice.application.external;

import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendErrorResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRegistrationReq;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRespUuid;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;

public interface BackendClientInterface {
    Result<String, BackendErrorResponse> registerCustomerOnBackend(BackendRegistrationReq req);
    Result<BackendRespUuid, BackendErrorResponse> getCustomerUuid(BackendRegistrationReq req);
}