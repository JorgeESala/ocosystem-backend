package com.ocosur.ocosystem.livechicken.expense;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, ExpenseCategoryCode>{
    
}
