package com.ocosur.ocosystem.livechicken.accounting.read.dto;

import java.math.BigDecimal;
import java.time.LocalDate;


public record AccountsPayableOpenDTO(
        Long id,

        Long debtorId,
        String debtorName,

        Long creditorId,
        String creditorName,
        Long solicitorId,
        String solicitorName,
        BigDecimal totalAmount,
        BigDecimal balance,
        LocalDate date) {
}
