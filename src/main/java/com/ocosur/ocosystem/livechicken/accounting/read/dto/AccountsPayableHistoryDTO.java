package com.ocosur.ocosystem.livechicken.accounting.read.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AccountsPayableHistoryDTO(
        Long movementId,
        String movementType,
        BigDecimal amount,
        BigDecimal balanceBefore,
        BigDecimal balanceAfter,
        LocalDate created_at,
        String compensation_folio,
        String note) {
}
