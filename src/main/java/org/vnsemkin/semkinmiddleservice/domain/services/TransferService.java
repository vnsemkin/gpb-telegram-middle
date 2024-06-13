package org.vnsemkin.semkinmiddleservice.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.TransferRequest;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.TransferResponse;
import org.vnsemkin.semkinmiddleservice.application.usecases.TransferMoneyService;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;


@Service
@RequiredArgsConstructor
public final class TransferService {
    private final TransferMoneyService transferMoneyService;
    public Result<TransferResponse, String> transfer(TransferRequest request) {
        return transferMoneyService.transferMoney(request);
    }
}
