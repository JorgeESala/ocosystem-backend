package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BatchItemResponseDTO(
        Long id,
        Long branchId,
        String branchName,
        BigDecimal kgTotal,
        BigDecimal pricePerKg,
        BigDecimal priceTotal,
        BigDecimal avgChickenWeight,
        LocalDate date,
        String provider,
        BigDecimal chickenQuantity) {
}
