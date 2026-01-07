package com.ocosur.ocosystem.livechicken.batchsale.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboundBatchSaleUpdateRequestDTO {
    @NotNull
    private Long id;
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
