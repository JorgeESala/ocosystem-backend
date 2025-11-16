package com.ocosur.ocosystem.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

@Data
@Builder
public class ReportEntryDTO {

    private Integer branchId;
    private String branchName;
    private Integer year;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private String frequency; 

    private BigDecimal totalSales;
    private BigDecimal totalExpenses;
    private BigDecimal totalProfit;
    private BigDecimal totalSold;
    private BigDecimal totalBought;

    // métricas opcionales
    private BigDecimal gut;
    private BigDecimal waste;
    private BigDecimal slaughteredChicken;
    private BigDecimal eggs;
    private BigDecimal eggCartons;
    private BigDecimal totalEggsSale;

    // ventas por categoría y producto
    private Map<String, BigDecimal> salesByCategory;
    private Map<String, BigDecimal> salesByProduct;
    private Map<String, BigDecimal> quantitiesByProduct;
    private Map<String, BigDecimal> quantitiesByCategory;
    private Map<String, BigDecimal> expensesByCategory;
}
