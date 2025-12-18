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
    private Long branchId;
    private String yearMonth;
    private BigDecimal totalSales;
    private BigDecimal eggs;
    private BigDecimal eggCartons;
    private BigDecimal eggsSale;
    // Desglose
    private Map<String, BigDecimal> salesByCategory;
    private List<WeeklyReportDTO> weeklyReports;
    private List<ProductReportDTO> productReports;
}
