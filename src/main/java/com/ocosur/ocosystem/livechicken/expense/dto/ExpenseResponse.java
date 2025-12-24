package com.ocosur.ocosystem.livechicken.expense.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpenseResponse {
    private Long id;
    private String reason;
    private String amount;
}
