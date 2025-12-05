package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class BatchSaleUpdateDTO {
    private Integer id;              // ← requerido
    private Integer batchId;         // puedes permitir cambiar de batch o no, tú decides
    private Integer employeeId;
    private BigDecimal quantitySold;
    private BigDecimal kgTotal;
    private BigDecimal saleTotal;
    private BigDecimal kgGut;
    private LocalDate date;
}

