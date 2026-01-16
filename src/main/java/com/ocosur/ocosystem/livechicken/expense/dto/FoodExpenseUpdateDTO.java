package com.ocosur.ocosystem.livechicken.expense.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FoodExpenseUpdateDTO {
    private Long cedisId;
    private BigDecimal weight;
}
