package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;

public record BatchCostDetail(
        Long batchId,
        String branchName,
        BigDecimal totalBatchCost,
        BigDecimal chickenQuantity,
        BigDecimal avgChickenWeight,
        BigDecimal pricePerKg,
        BigDecimal quantitySoldInRange,
        BigDecimal kgSoldInRange,
        BigDecimal computedCostForRange,
        BigDecimal totalSalesInRange, // nuevo
        BigDecimal aspKg // nuevo
) {
}
