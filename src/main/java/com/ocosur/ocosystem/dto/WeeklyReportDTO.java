package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WeeklyReportDTO {
    private Integer branchId;
    private OffsetDateTime weekStart;
    private BigDecimal totalSales;
    private BigDecimal totalExpenses;
    private BigDecimal totalProfit;
    private BigDecimal totalSold;
    private BigDecimal totalBought;
    private BigDecimal gut;
    private BigDecimal waste;
    private BigDecimal eggQuantity;
    private BigDecimal eggCartonsQuantity;
    private BigDecimal eggsSales;
    private Map<String, BigDecimal> salesByCategory;
    private Map<String, BigDecimal> salesByProduct;
    private Map<String, BigDecimal> expensesByCategory;
    
    private List<DailyReportDTO> dailyReports;
    private List<ProductReportDTO> productReports;
}
