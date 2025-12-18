package com.ocosur.ocosystem.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BatchSaleSearchRequestDTO {

    @NotNull
    private List<Long> branchIds;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    // Filtros opcionales
    private Long batchId;
    private Long employeeId;

    @AssertTrue(message = "startDate must be before or equal to endDate")
    public boolean isValidDateRange() {
        if (startDate == null || endDate == null)
            return true;
        return !startDate.isAfter(endDate);
    }
}
