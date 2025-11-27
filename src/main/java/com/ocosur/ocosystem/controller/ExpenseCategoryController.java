package com.ocosur.ocosystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.model.ExpenseCategory;
import com.ocosur.ocosystem.service.ExpenseCategoryService;

@RestController
@CrossOrigin
@RequestMapping("/api/expense-categories")
public class ExpenseCategoryController {

    @Autowired
    private ExpenseCategoryService expenseCategoryService;

    @GetMapping()
    public List<ExpenseCategory> getExpenseCategories() {
        return expenseCategoryService.getAllCategories();
    }

    @PostMapping()
    public ExpenseCategory saveExpenseCateogry(@RequestBody ExpenseCategory expenseCategory) {
        return expenseCategoryService.saveExpenseCategory(expenseCategory);
    }

}
