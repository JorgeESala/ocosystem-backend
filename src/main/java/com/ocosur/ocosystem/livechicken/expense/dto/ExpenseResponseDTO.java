package com.ocosur.ocosystem.livechicken.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ocosur.ocosystem.livechicken.expense.ExpenseCategoryCode;
import com.ocosur.ocosystem.livechicken.expense.ExpenseType;
import com.ocosur.ocosystem.livechicken.expense.VehicleExpenseCategory;

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

    private ExpenseCategoryCode categoryCode;
    private String categoryName;
    private ExpenseType expenseType;

    private String reason;
    private BigDecimal amount;
    private LocalDate date;

    // --- Food ---
    private String cedisName;
    private BigDecimal weight;

    // --- Vehicle ---
    private String vehicleName;
    private String employeeName;
    private VehicleExpenseCategory vehicleCategory;

    // --- Fuel ---
    private String routeName;
}

