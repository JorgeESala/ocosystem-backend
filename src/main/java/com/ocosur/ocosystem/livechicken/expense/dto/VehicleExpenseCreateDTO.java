package com.ocosur.ocosystem.livechicken.expense.dto;

import com.ocosur.ocosystem.livechicken.expense.VehicleExpenseCategory;

import lombok.Data;

@Data
public class VehicleExpenseCreateDTO {
    private Long vehicleId;
    private Long employeeId;
    private VehicleExpenseCategory category;
}
