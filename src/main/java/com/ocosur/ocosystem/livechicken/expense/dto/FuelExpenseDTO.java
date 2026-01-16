package com.ocosur.ocosystem.livechicken.expense.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FuelExpenseDTO {
    private Long vehicleId;
    private Long employeeId;
    private Long routeId;
}
