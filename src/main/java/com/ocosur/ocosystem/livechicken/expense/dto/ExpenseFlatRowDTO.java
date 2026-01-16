package com.ocosur.ocosystem.livechicken.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ocosur.ocosystem.livechicken.expense.ExpenseCategoryCode;
import com.ocosur.ocosystem.livechicken.expense.ExpenseType;
import com.ocosur.ocosystem.livechicken.expense.VehicleExpenseCategory;

public record ExpenseFlatRowDTO(
        Long id,
        ExpenseCategoryCode categoryCode,
        String categoryName,
        ExpenseType expenseType,
        String reason,
        BigDecimal amount,
        LocalDate date,

        // Food
        Long cedisId,
        String cedisName,
        BigDecimal weight,

        // Vehicle
        Long vehicleId,
        String vehicleName,
        Long employeeId,
        String employeeName,
        VehicleExpenseCategory vehicleCategory,

        // Fuel
        Long routeId,
        String routeName) {
}
