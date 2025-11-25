package com.ocosur.ocosystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ocosur.ocosystem.model.Expense;
import com.ocosur.ocosystem.service.ExpenseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin
@Controller
@RequestMapping("/api/expenses")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;
    
    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses() {
        return new ResponseEntity<List<Expense>>(expenseService.getExpenses(), HttpStatus.OK);
    }

    @PostMapping()
    public Expense saveExpense(@RequestBody Expense expense){
        return expenseService.saveExpense(expense);
    }
    
}
