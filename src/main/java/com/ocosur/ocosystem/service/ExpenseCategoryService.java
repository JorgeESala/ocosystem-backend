package com.ocosur.ocosystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.model.ExpenseCategory;
import com.ocosur.ocosystem.repository.BranchesExpenseCategoryRepository;

@Service
public class ExpenseCategoryService {
    @Autowired
    BranchesExpenseCategoryRepository expenseCategoryRepository;

    public List<ExpenseCategory> getAllCategories() {
        return expenseCategoryRepository.findAll();
    }
    public ExpenseCategory saveExpenseCategory(ExpenseCategory expenseCategory) {
        return expenseCategoryRepository.save(expenseCategory);
    }
}
