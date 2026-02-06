package com.ocosur.ocosystem.livechicken.accounting.read.dto;

import java.math.BigDecimal;

public record AccountsPayableAgingDTO(
        Long accountsPayableId,
        Long debtorId,
        Long creditorId,
        BigDecimal balance,
        String agingBucket
) {}
