package org.vnsemkin.semkinmiddleservice.application.external;

import org.vnsemkin.semkinmiddleservice.application.dtos.back.ResultBackendDto;

public interface BackendClientInterface {
    ResultBackendDto<?> registerCustomerOnBackend(long customerId);
    ResultBackendDto<?> getCustomerUuid(long id);
}