package com.ocosur.ocosystem.livechicken.expense.dto;

import lombok.Data;

@Data
public class FuelExpenseCreateDTO {
    private Long vehicleId;
    private Long employeeId;
    private Long routeId;
}
