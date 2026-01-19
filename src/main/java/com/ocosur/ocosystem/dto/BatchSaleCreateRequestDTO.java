package com.ocosur.ocosystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BatchSaleCreateRequestDTO {

    private Long batchId;
    private Long employeeId;
    private Long clientId;
    private BigDecimal quantitySold;
    private BigDecimal kgTotal;
    private BigDecimal saleTotal;
    private BigDecimal kgGut;
    private LocalDate date;
}
