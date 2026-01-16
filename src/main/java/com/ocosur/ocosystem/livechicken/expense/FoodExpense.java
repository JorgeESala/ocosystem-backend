package com.ocosur.ocosystem.livechicken.expense;

import java.math.BigDecimal;

import com.ocosur.ocosystem.core.cedis.model.Cedis;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(schema = "live_chicken", name = "food_expense")
public class FoodExpense {

    @Id
    private Long expenseId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "expense_id")
    private Expense expense;

    @ManyToOne
    @JoinColumn(name = "cedis_id")
    private Cedis cedis;

    @Column(nullable = false)
    private BigDecimal weight;
}
