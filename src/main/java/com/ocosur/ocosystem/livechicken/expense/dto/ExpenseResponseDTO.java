package com.ocosur.ocosystem.livechicken.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseResponseDTO {
    private Long id;
    private String reason;
    private BigDecimal amount;
    private LocalDate date;
}
