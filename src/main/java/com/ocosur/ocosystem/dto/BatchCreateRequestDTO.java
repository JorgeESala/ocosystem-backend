package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BatchCreateRequestDTO(
        Long branchId,
        BigDecimal kgTotal,
        BigDecimal pricePerKg,
        LocalDate date,
        String provider,
        BigDecimal chickenQuantity) {
}
