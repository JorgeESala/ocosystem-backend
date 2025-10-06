package com.ocosur.ocosystem.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
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
    String name;
    BigDecimal paid;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    Branch branch;
    @Column(columnDefinition = "timestampz")
    OffsetDateTime date;
    BigDecimal quantity;
    @ManyToOne
    @JoinColumn(name = "unit_id")
    Measurement_unit unit;

    @ManyToOne
    @JoinColumn(name = "category_id")
    ExpenseCategory category;

}
