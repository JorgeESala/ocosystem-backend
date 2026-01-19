package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class BatchSaleUpdateDTO {
    private Long id;              // ← requerido
    private Long batchId;         // puedes permitir cambiar de batch o no, tú decides
    private Long employeeId;
    private Long clientId;
    private BigDecimal quantitySold;
    private BigDecimal kgTotal;
    private BigDecimal saleTotal;
    private BigDecimal kgGut;
    private LocalDate date;
}

