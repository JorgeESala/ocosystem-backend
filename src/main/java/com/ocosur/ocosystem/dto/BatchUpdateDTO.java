package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
@Data
public class BatchUpdateDTO {
    private Integer branchId;
    private BigDecimal kgTotal;
    private BigDecimal pricePerKg;
    private LocalDate date;
    private String provider;
    private BigDecimal chickenQuantity;

    
}
