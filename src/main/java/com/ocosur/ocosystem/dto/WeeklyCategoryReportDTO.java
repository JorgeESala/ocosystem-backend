package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
 
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeeklyCategoryReportDTO {
    private Long branchId;
    private Long categoryId;
    private OffsetDateTime weekStart;
    private BigDecimal sales;
    private BigDecimal expenses;
    private BigDecimal profit;
    private BigDecimal sold;   
    private BigDecimal bought; 
}