package com.ocosur.ocosystem.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String reason;
    BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    Branch branch;
    LocalDate date;
    @ManyToOne
    @JoinColumn(name = "category_id")
    ExpenseCategory category;

}
