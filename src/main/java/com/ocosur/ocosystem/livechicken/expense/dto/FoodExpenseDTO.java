package com.ocosur.ocosystem.livechicken.expense.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FoodExpenseDTO {
    private Long cedisId;
    private BigDecimal weight;
}

