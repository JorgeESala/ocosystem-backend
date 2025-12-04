package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ProfitReport(
        LocalDate start,
        LocalDate end,
        BigDecimal totalSales, // suma de saleTotal (ingresos)
        BigDecimal totalExpenses, // suma de expenses.amount
        BigDecimal totalChickenCostsProRated, // suma de computedCostForRange
        BigDecimal profit, // totalSales - totalChickenCostsProRated - totalExpenses
        List<BatchCostDetail> batchDetails) {
}