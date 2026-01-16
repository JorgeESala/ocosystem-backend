package com.ocosur.ocosystem.livechicken.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ocosur.ocosystem.livechicken.expense.ExpenseCategoryCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDetailResponseDTO {

    private Long id;

    private ExpenseCategoryCode categoryCode;

    private String reason;
    private BigDecimal amount;
    private LocalDate date;

    private FoodExpenseDTO food;
    private FuelExpenseDTO fuel;
    private VehicleExpenseDTO vehicle;
}
