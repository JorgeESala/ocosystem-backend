package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeeklyReportDTO {
    private Integer branchId;
    private OffsetDateTime weekStart;
    private BigDecimal totalSales;
    private BigDecimal totalExpenses;
    private BigDecimal profit;
    private BigDecimal totalSold;
    private BigDecimal totalBought;
    private BigDecimal gut;
    private BigDecimal waste;
    // ventas agrupadas por categoría
    private Map<String, BigDecimal> salesByCategory;

    // ventas agrupadas por producto
    private Map<String, BigDecimal> salesByProduct;

    // opcional: gastos agrupados por categoría
    private Map<String, BigDecimal> expensesByCategory;
}
