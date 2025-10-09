package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MonthlyCategoryReportDTO {
    private Integer branchId;
    private YearMonth yearMonth; // formato YYYY-MM
    private Integer categoryId;

    // Totales
    private BigDecimal totalSales;
    private BigDecimal totalExpenses;
    private BigDecimal totalProfit;
    private BigDecimal totalSold;   
    private BigDecimal totalBought;
    private BigDecimal gut;
    private BigDecimal waste;

    // Reportes desglosados
    private Map<String, BigDecimal> salesByProduct;       
    private Map<String, BigDecimal> quantitiesByProduct; 

    private List<WeeklyReportDTO> weeklyReports; // Reportes semanales solo de esa categoría
}
