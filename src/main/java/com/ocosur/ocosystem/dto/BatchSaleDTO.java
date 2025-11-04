package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
@Data
public class BatchSaleDTO {
    private Integer batchId;
    private BigDecimal quantitySold;
    private BigDecimal kgTotal;
    private BigDecimal saleTotal;
    private BigDecimal kgGut;
    private LocalDate date;
}
