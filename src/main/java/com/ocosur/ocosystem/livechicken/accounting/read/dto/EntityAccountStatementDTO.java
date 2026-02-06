package com.ocosur.ocosystem.livechicken.accounting.read.dto;
import java.math.BigDecimal;
import java.time.LocalDate;

public record EntityAccountStatementDTO(
        Long debtorEntityId,
        Long creditorEntityId,
        LocalDate movementDate,
        String movementType,
        BigDecimal balanceBefore,
        BigDecimal amount,
        BigDecimal balanceAfter,
        String folio,
        String note
) {}
