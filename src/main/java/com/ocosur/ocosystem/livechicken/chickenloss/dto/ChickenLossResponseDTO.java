package com.ocosur.ocosystem.livechicken.chickenloss.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChickenLossResponseDTO {
    private Long id;
    private Integer quantity;
    private BigDecimal weight;
    private BigDecimal lossAmount;
    private Long batchId;
    private LocalDate date;
}
