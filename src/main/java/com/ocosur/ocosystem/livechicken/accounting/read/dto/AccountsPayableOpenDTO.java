package com.ocosur.ocosystem.livechicken.accounting.read.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountsPayableOpenDTO(
        Long accountsPayableId,

        Long debtorId,
        String debtorName,

        Long creditorId,
        String creditorName,

        BigDecimal totalAmount,
        BigDecimal balance,
        LocalDateTime createdAt) {
}
