package org.vnsemkin.semkinmiddleservice.application.dtos.front;

import java.math.BigDecimal;

public record AccountRegistrationResponse(String accountName, BigDecimal balance, String info) {
}