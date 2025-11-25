package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ocosur.ocosystem.model.Branch;
import com.ocosur.ocosystem.model.ExpenseCategory;
import lombok.Data;

@Data
public class ExpenseDTO {
    Integer id;
    String reason;
    BigDecimal amount;
    Branch branch;
    LocalDate date;
    ExpenseCategory category;
}
