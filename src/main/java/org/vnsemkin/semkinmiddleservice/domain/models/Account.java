package org.vnsemkin.semkinmiddleservice.domain.models;

import java.math.BigDecimal;

public record Account(long id, String uuid, String accountName, BigDecimal balance) {
}