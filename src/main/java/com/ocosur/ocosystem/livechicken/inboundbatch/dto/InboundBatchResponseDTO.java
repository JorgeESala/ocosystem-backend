package com.ocosur.ocosystem.livechicken.inboundbatch.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class InboundBatchResponseDTO {

    private Long id;
    private Long supplierId;
    private String supplierName;
    private LocalDate date;
    private BigDecimal realWeight;
    private BigDecimal declaredWeight;
    private Integer chickenQuantity;
    private BigDecimal pricePerKg;
    private BigDecimal totalPaid;
    private BigDecimal avgWeight;
}
