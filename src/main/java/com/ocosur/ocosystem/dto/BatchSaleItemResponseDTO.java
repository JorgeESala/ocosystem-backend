package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BatchSaleItemResponseDTO(
    Long id,
    Long batchId,
    BigDecimal quantitySold,
    BigDecimal kgTotal,
    BigDecimal kgGut,
    BigDecimal saleTotal,
    Long employeeId,
    String employeeName,
    LocalDate date
) {}
