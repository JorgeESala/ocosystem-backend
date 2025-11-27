package com.ocosur.ocosystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocosur.ocosystem.model.ExpenseCategory;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Integer> {
    
}
