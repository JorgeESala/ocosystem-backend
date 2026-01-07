package com.ocosur.ocosystem.livechicken.batchsale.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InboundBatchSaleCreateRequestDTO {
    @NotNull
    private Long batchId;
    @NotNull
    private BigDecimal quantitySold;
    @NotNull
    private BigDecimal kgSold;
    @NotNull
    private BigDecimal kgSent;
    @NotNull
    private Long employeeId;
    @NotNull
    private BigDecimal saleTotal;
    @NotNull
    private LocalDate date;
    @Nullable
    private Long routeId;
}
