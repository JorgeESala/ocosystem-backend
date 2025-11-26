package com.ocosur.ocosystem.dto;
import java.math.BigDecimal;
import java.time.LocalDate;


public record ExpenseDTO (
    String reason,
    BigDecimal amount,
    Integer branchId,
    LocalDate date,
    Integer categoryId
){}
