package com.ocosur.ocosystem.livechicken.expense.dto;

import com.ocosur.ocosystem.livechicken.expense.VehicleExpenseCategory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleExpenseDTO {
    private Long vehicleId;
    private Long employeeId;
    private VehicleExpenseCategory category;
}
