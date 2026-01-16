package com.ocosur.ocosystem.livechicken.expense;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "LiveChickenExpenseCategory")
@Table(schema = "live_chicken", name = "expense_category")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseCategory {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private ExpenseCategoryCode code;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ExpenseType expenseType;
}
