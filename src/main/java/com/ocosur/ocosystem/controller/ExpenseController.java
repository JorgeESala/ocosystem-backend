package com.ocosur.ocosystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.dto.ExpenseDTO;
import com.ocosur.ocosystem.model.Expense;
import com.ocosur.ocosystem.service.ExpenseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;
    
    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses() {
        return new ResponseEntity<List<Expense>>(expenseService.getExpenses(), HttpStatus.OK);
    }   
    @GetMapping("/{branchId}")
    public ResponseEntity<List<Expense>> getExpensesByBranch(@PathVariable Integer branchId) {
        return new ResponseEntity<List<Expense>>(expenseService.getLatestExpensesByBranch(), HttpStatus.OK);
    }

    @PostMapping()
    public Expense saveExpense(@RequestBody ExpenseDTO expense){
        return expenseService.saveExpense(expense);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Integer id, @RequestBody ExpenseDTO expenseDTO) {
        return new ResponseEntity<Expense>(expenseService.updateExpense(id, expenseDTO), HttpStatus.OK);
    }
    
}
