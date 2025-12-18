package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.util.List;

public record BatchSearchResponseDTO(
        List<BatchItemResponseDTO> items,
        BigDecimal totalKg,
        BigDecimal totalCost,
        long count) {
}
