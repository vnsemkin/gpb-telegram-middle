package org.vnsemkin.semkinmiddleservice.application.dtos.back;

import java.math.BigDecimal;

public record AccountInfoResponse(String accountId, String accountName, BigDecimal balance) {
}