package com.ocosur.ocosystem.livechicken.batchsale.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InboundBatchSaleCreateRequestDTO {
    private Long batchId;
    private BigDecimal quantitySold;
    private BigDecimal kgSold;
    private BigDecimal kgSent;
    private Long employeeId;
    private BigDecimal saleTotal;
    private LocalDate date;
    private Long routeId;
}
