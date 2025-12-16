package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.util.List;

public record BatchSaleSearchResponseDTO(
        List<BatchSaleItemResponseDTO> items,
        BigDecimal totalSales,
        BigDecimal totalKg,
        Long totalCount) {
}
