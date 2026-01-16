package com.ocosur.ocosystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity(name = "BranchesExpenseCategory")
@Table(schema = "public", name = "expense_category")
@Data
public class ExpenseCategory {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
}
