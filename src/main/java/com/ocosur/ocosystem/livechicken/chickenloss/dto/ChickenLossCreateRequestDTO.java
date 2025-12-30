package com.ocosur.ocosystem.livechicken.chickenloss.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChickenLossCreateRequestDTO {
    private Integer quantity;
    private BigDecimal weight;
    private BigDecimal lossAmount;
    private Long batchId;
    private LocalDate date;
}
