package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyReportDTO {
    private Integer branchId;
    private String yearMonth;
    private BigDecimal totalSales;
    private BigDecimal totalExpenses;
    private BigDecimal totalProfit;
    private BigDecimal totalSold;
    private BigDecimal totalBought;

    // Desglose
    private Map<String, BigDecimal> salesByCategory;
    private Map<String, BigDecimal> expensesByCategory;

    private List<WeeklyReportDTO> weeklyReports;
}
