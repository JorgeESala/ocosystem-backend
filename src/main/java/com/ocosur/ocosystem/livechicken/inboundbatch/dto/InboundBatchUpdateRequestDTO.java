package com.ocosur.ocosystem.livechicken.inboundbatch.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class InboundBatchUpdateRequestDTO {

    @NotNull
    private Long supplierId;

    @NotNull
    private LocalDate date;

    @NotNull
    @Positive
    private BigDecimal realWeight;
    
    @NotNull
    @Positive
    private BigDecimal declaredWeight;

    @NotNull
    @Positive
    private Integer chickenQuantity;

    @NotNull
    @Positive
    private BigDecimal pricePerKg;
}
