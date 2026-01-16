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
public class ExpenseUpdateRequestDTO {
    private String reason;
    private BigDecimal amount;
    private LocalDate date;

    // Optional payloads
    private FuelExpenseUpdateDTO fuel;
    private FoodExpenseUpdateDTO food;
    private VehicleExpenseUpdateDTO vehicle;
}
