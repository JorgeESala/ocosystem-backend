package com.ocosur.ocosystem.livechicken.batchsale.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class InboundBatchSaleResponseDTO {
    private Long id;
    private Long batchId;
    private BigDecimal quantitySold;
    private BigDecimal kgSold;
    private BigDecimal kgSent;
    private Long employeeId;
    private String employeeName;
    private BigDecimal saleTotal;
    private LocalDate date;
    private Long routeId;
    private String routeName;
}
