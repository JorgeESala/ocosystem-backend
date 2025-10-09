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
public class DailyReportDTO {

    private Integer branchId;              
    private OffsetDateTime date;                  

    // Totales
    private BigDecimal totalSales;         
    private BigDecimal totalExpenses;   
    private BigDecimal totalProfit;        
    private BigDecimal totalSold;         
    private BigDecimal totalBought;        
    private BigDecimal gut;               
    private BigDecimal waste;
    private BigDecimal slaughteredChicken;              
    private BigDecimal eggQuantity;
    private BigDecimal eggCartonsQuantity;
    private BigDecimal eggsSales;

    // Detalles desglosados
    private Map<String, BigDecimal> salesByCategory;       
    private Map<String, BigDecimal> salesByProduct;      
    private Map<String, BigDecimal> quantitiesByProduct;   
    private Map<String, BigDecimal> expensesByCategory;    
}
