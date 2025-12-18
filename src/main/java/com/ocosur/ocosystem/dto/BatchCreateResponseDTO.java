package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BatchCreateResponseDTO(
        Long id,
        Long branchId,
        BigDecimal kgTotal,
        BigDecimal pricePerKg,
        BigDecimal priceTotal,
        BigDecimal avgChickenWeight,
        LocalDate date,
        String provider,
        BigDecimal chickenQuantity) {
}
