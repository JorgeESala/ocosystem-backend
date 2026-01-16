package com.ocosur.ocosystem.livechicken.expense;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveChikenExpenseCategoryRepository extends JpaRepository<ExpenseCategory, ExpenseCategoryCode>{
}
